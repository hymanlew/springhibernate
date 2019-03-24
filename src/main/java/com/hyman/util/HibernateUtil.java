package com.hyman.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// 定义工具类，不可创建实例，不可被更改
public final class HibernateUtil {

    private static SessionFactory sessionFactory;
    private HibernateUtil(){

    }

    static {
        Configuration config = new Configuration();
        config.configure();
        sessionFactory = config.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

}
