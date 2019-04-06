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
  */
}
