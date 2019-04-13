package com.hyman.util;

public class OtherSetting {
    /**
     Hiberante懒加载特性：
     其目的是为了减少与数据库的交互，从而提高执行效率。
     在 Hibernate中的 load()方法就拥有懒加载的特性。Load()方法就是在查询某一条数据的时候并不会直接将这条数据以指定对象的形式
     来返回，而是在你真正需要使用该对象里面的一些属性的时候才会去数据库访问并得到数据。他的好处就是可以减少程序本身因为与数据
     库频繁的交互造成的处理速度缓慢。

     另外，在查询更新等平常操作中，也要注意到懒加载的情况（many2one 例子）：
     Hibernate 允许对关联对象、属性进行延迟加载，但是必须保证延迟加载的操作限于同一个 Hibernate Session 范围之内进行。如果
     Service 层返回一个启用了延迟加载功能的领域对象给 Web 层，当 Web 层访问到那些需要延迟加载的数据时，由于加载领域对象的
     Hibernate Session 已经关闭，这些导致延迟加载数据的访问异常。

     然而在 Hibernate懒加载的时候，返回的对象是并不是空的（打印对象的 getClass()方法来验证），打印出来的结果并不是null，而是
     一个加了一堆很奇怪的字符的对象类。可以肯定的是懒加载的对象并不是空，而且这个对象的类型不是要查询的类。
     我们管它叫做代理对象，而这个对象所属的类是查询的类的子类，是 Hibernate自动实现的一个子类。这个子类的特点是：当你访问person
     对象的某一个属性的时候，他会自动查询数据库中对应这个对象的数据并返回，这就是为什么在创建对象关系映射的时候要求实体类不能够
     为 final类型的原因了。


     hibernate 的懒加载是通过 asm 和 cglib 两个包来实现的，表现方式有：
     1，session 的 load 方法。
     2，one-to-one 必须同时满足三个条件才能实现懒加载：lazy != false，fetch = select，constrained = true。
     3，one-to-many，many-to-one 和 many-to-many：都要求 lazy != false。fetch = select。（因为当 fetch 为 join时，是关联查
        询即一次性的就把数据给取出来了，那么再配置 lazy 它也就失效了，也就没什么意义了）。
     4，能够懒加载的对象都是被改写过的代理对象，当相关联的 session 没有关闭时，访问这些懒加载对象（代理对象）的属性（getid 和
        getclass 除外），hibernate 都会初始化这些代理，或者使用 Hibernate.initialize(proxy) 初始化。当相关联的 session 关闭
        后，再访问这些懒加载对象就会抛异常。

     需要注意，因为主表（父类）不会有 constrained = true（外键约束），所以主表没有懒加载。是因为在查询主表时，由于没有外键约束，
     所以它不确定是否含有某个子类，所以它只能去访问数据库，而这也就不是懒加载了（但是此情况只适应于 one-to-one 的场景，因为它实
     时加载不会影响到太大的性能开销。但是在 one-to-many，many-to-many 时实时加载会造成很大的开销，所以 hibernate 会假设父类拥
     有所有的子类，就是使用懒加载）。
     而在查询子表时，由于有外键约束，外键就代表了它肯定有其对应的父类，所以它就不需要去访问数据库，而这就是懒加载的原因）。

     所以处理懒加载异常的方式有三种：
     1，在 load 查询的代码中直接调用对象的属性方法，使得 hibernate去查询数据库，来初始化对象。
     2，使用  Hibernate.initialize(proxy) 初始化对象。
     3，修改配置文件中的 lazy 为 false，或者修改 fetch 为 join（关联表查询）。但要注意这种方式在一对多，多对多场景时，会增加数
        据库的连接次数，并获得全部的数据，也会造成不必要的资源和性能的浪费，甚至是数据库崩溃。


     Spring 为我们解决 Hibernate的 Session的关闭与开启问题及懒加载问题：
     它提供了 OpenSessionInViewFilter 过滤器为我们很好的解决了这个问题。其主要功能是把一个Hibernate Session和一次完整的请求
     过程对应的线程相绑定。目的是为了实现"Open Session in View"的模式。例如它允许在事务提交之后延迟加载显示所需要的对象。
     其主要意思是在发起一个页面请求时打开Hibernate的Session，一直保持这个Session，使得Hibernate的Session的生命周期变长，直到
     这个请求结束。
     OpenSessionInViewFilter 过滤器将 Hibernate Session 绑定到请求线程中，它将自动被 Spring 的事务管理器探测到。所以 OpenSession
     InViewFilter 适用于 Service 层使用HibernateTransactionManager 或 JtaTransactionManager 进行事务管理的环境，也可以用于
     非事务只读的数据操作中。

     <filter>
     <filter-name>OpenSessionInViewFilter</filter-name>
     <filter-class>org.springframework.orm.hibernate3.support.OpenSessionInViewFilter</filter-class>
     <init-param>
     <param-name>flushMode</param-name>
     <param-value>AUTO</param-value>
     </init-param>
     </filter>
     <filter-mapping>
     <filter-name>OpenSessionInViewFilter</filter-name>
     <url-pattern>/*</url-pattern>
     </filter-mapping>

     在 springboot 中的配置是：
     FilterRegistrationBean registrationBean = new FilterRegistrationBean();
     OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
     registrationBean.setFilter(filter);
     registrationBean.setOrder(5);
     return registrationBean;


     但是 hibernate 在多对多的操作和性能方面都不太理想，实现很麻烦。所以它的多对多映射使用较少。实际使用中最好转换为一对多的对
     象模型，hibernate 会自动创建中间关联表，形成两个一对多的关系。
     而 mybatis 就可以直接级联查询 collections。如果是两张表关联的话，则可以 one-to-many，many-to-one。

     并且在 hibernate 中这种双向的（在多方，一方都进行了配置的情况）多对多，一对多的关系只适用于多的一方数据量较小的情况下。此
     时框架在外连接查询时性能也不会差太多。但是在多方数据量很大的时候，这种外连接查询就会很影响性能。所以可以设置为单向的配置，
     即只在多方配置 many to one（多对一），而在一方就不再配置，需要外连接查询的时候再手动查询即可。
     多对多的情况下，就更是如此了就不要再进行配置，否则会严重影响性能。

     需要注意的是，在默认的情况下：
     hibernate 对于关联对象的查询（例如 emp，department的多对一，一对多查询）是用两条 sql 语句进行查询的。是先查主表，然后再根
     据外键去查出另外一张表的数据。
     而多对多的关联查询（例如 user，roles的查询），也是用两条 sql，先查主表，然后通过中间表内连接第三张表去查另外一方的数据。
     只有一对一关系的查询，才是只有一条外连接的 sql 语句，然后根据外键去查询出所需要的数据。


     在一对多关联对象的查询配置中，对于集合配置的选择：
     一般是使用 set（因为数据不可重复，且配置简单）。
     而 list的主要作用是可以对数据进行排序。
     map 的使用，就看需求是不是键值对的存储需求，一般用不到。
     array 数组类型的配置，使用的就更少。因为它对于数据的增删操作很不方便。


     hibernate 的缓存：
     1，一级缓存，session 级别的共享，save，update，saveOrUpdate，load，get，list，iterate，lock 方法都是使用一级缓存。但要
        注意一级缓存不控制缓存的数量，所以大批量操作时可能会造成内存溢出。可以用 evict，clear 方法清空缓存。并且当 session 关
        闭后缓存也会被清空，即它的作用范围仅限于在一个 session 中。
        query 方法不能使用一级缓存
     2，二级缓存，sessionFactory 级别的共享，它是直接使用的第三方缓存技术框架来实现的。默认就是打开状态。

     3，二级缓存的结构（分四类）：
         1、Class Cache Region：类级别缓存，主要用于存储PO（实体）对象。
         2、Collection Cache Region：集合级别缓存，用于存储集合数据。
         3、Query Cache Region：查询缓存，会缓存一些常用查询语句的查询结果。
         4、Update TimesTamps：更新时间戳缓存，存放与查询结果相关的表在进行操作时的时间戳，hibernate通过更新时间戳缓存区判断被缓存的查询结果是否过期。

     4，缓存的并发访问策略：
         1、只读型（Read-Only）：提供Serializable数据隔离级别，对于从来不修改的数据可以采用。
         2、读写型（Read-Write）：提供Read Commited数据隔离级别，对于经常读但很少被修改的数据，可以采用，可防止脏读。
         3、非严格读写（Nonstrict-read-write）：不保证缓存与数据库中数据的一致性，提供Read Uncommited事务隔离级别，对于极少被修改，且允许脏读的数据，可以采用。
         4、事务型（Transactional）：提供Repeatable Read事务隔离级别，对于经常读但是少更改的数据，可采用，可防止脏读和不可重复读。

     5，ehcache 不支持事务型，但支持其他三种模式。
     6，调试时使用 log4j的 log4j.logger.org.hibernate.cache=debug，可以看到ehcache的操作过程。但实际应用发布时候，请注释掉，以免影响性能。

     7，请注意：在查询缓存中，ehcache并不缓存结果集中所包含的实体的确切状态；它只缓存这些实体的标识符属性的值、以及各值类型的结
        果。需要将打印sql语句与最近的cache内容相比较，将不同之处修改到cache中，所以查询缓存通常会和二级缓存一起使用。
        并且当用 Hibernate的方式修改表数据 (save,update,delete等等)，这时 EhCache会自动把缓存中关于此表的所有缓存全部删除掉
        (这样能达到同步)。但对于数据经常修改的表来说，可能就失去缓存的意义了(不能减轻数据库压力)；
  */
}
