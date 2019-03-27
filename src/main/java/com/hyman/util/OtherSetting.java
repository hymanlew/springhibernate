package com.hyman.util;

public class OtherSetting {
    /**
     Hiberante懒加载特性：
     其目的是为了减少与数据库的交互，从而提高执行效率。
     在 Hibernate中的 load()方法就拥有懒加载的特性。Load()方法就是在查询某一条数据的时候并不会直接将这条数据以指定对象的形式
     来返回，而是在你真正需要使用该对象里面的一些属性的时候才会去数据库访问并得到数据。他的好处就是可以减少程序本身因为与数据
     库频繁的交互造成的处理速度缓慢。

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





     */
}
