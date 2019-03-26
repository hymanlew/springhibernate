package com.hyman.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// 定义工具类，不可创建实例，不可被更改

/**
 * HQL（hibernate query language)，即hibernate提供的面向对象的查询语言。查询的是对象以及对象的属性，因此是区分大小写的。
 * HQL是面向对象的查询语言，可以用来查询全部的数据！
 *
 * SQL（Struct query language)，结构化查询语言。查询的是表以及列，不区分大小写。
 * 有时候如果 SQL是非常复杂的，我们不能靠 HQL查询来实现功能的话，我们就需要使用原生的 SQL来进行复杂查询了！但是它有一个缺陷：
 * 它是不能跨平台的，因为我们在主配置文件中已经配置了数据库的“方言“了。
 *
 * QBC（query by criteria)，完全面向对象的查询。HQL查询是需要SQL的基础的，因为还是要写少部分的SQL代码，而QBC查询就是完全的面
 * 向对象查询。但是用得比较少。
 */
public final class HibernateUtil {

    private static SessionFactory sessionFactory;
    private HibernateUtil(){

    }

    /**
     * Configuration 是配置管理类（管理配置文件的一个类），它有一个子类 AnnotationConfiguration 可以使用注解来代替 XML配置
     * 文件来配置相对应的信息。
     *
     * Session 是Hibernate最重要的对象，Session维护了一个连接（Connection），只要使用 Hibernate操作数据库，都需要用到Session
     * 对象。它为我们提供了 save，update，get，delete，find 对应的方法来实现！
     */
    static {
        Configuration config = new Configuration();
        // 读取指定的主配置文件，不给参数就默认加载 src/目录下的 hibernate.cfg.xml文件
        config.configure();
        // 根据配置生成Session工厂
        sessionFactory = config.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        // 打开一个新的 Session
        return sessionFactory.openSession();
    }

}
