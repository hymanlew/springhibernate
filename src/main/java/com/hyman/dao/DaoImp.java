package com.hyman.dao;

public class DaoImp  {

    public static void t1(){
        /**
         * 在 SSH 框架整合中，dao 要使用 public class DaoImpl extends HibernateDaoSupport implements Dao {}，可以直接使用
         * spring 封装的 getHibernateTemplate() 相对应的方法来执行。
         *
         * 并且在使用参数查询（getHibernateTemplate.find(String hql, Object... values)）时，传的值不能添加单引号 '，在使用
         * 不带参数查询（getHibernateTemplate.find(String hql)）时需要添加单引号 '，否则会报错！
         *
         * getHibernateTemplate() 是 Hibernate为了简化相关的增删改查操作，而封装了数据库的一些例行通用操作，可以直接使用，以
         * 此来提升开发效率。但它是在 Spring整合Hibernate的时候才用到的，由 Spring对 Hibernate相关的操作对象进行封装。且DAO
         * 层实现类必须继承 HibernateDaoSupport。
         * 它封装了 SessionTest test1 中的相关操作。
         *
         * 并且在配置中一定要传入 sessionFactory 节点：
            <bean id="dao" class="com.hnjz.base.dao.DaoImpl">
                <property name="sessionFactory" ref="sessionFactory" />
            </bean>
         *
         *
         * 1，getSession()和getHibernateTemplate() 都可以自动释放连接，首先你的配置要正确。但是在一个线程内 getSession会
         *    get很多个session（就是开很多个会话、连接），很可能导致数据库连接超过上限。所以推荐使用 getHibernateTemplate。
         *
         * 2，如果有些语句无法用 getHibernateTemplate实现，可以使用 getHibernateTemplate.execute使用 HibernateCallback回调
         *    接口（在该接口的内部 doInHibernate方法中，它的返回值，就是 execute或 executeFind方法的返回值）。另可以设定
         *    HibernateTemplate的 AllowCreate为True，并在finally 中关闭Session。
         *    也可以将true作为参数传递到super.getSession(..)方法中取得Session。这样也可以，就是麻烦点。
         *
         */


    }
}
