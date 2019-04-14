package com.hyman.demo;

import com.hyman.entity.*;
import com.hyman.util.CacheUtil;
import com.hyman.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

public class CacheTest {

    public static User getUser(){
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            User user = session.get(User.class,1);
            Hibernate.initialize(user);

            return user;
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("======== "+e.getMessage());
        }finally {
            if(session != null){
                session.close();
            }
        }
        return null;
    }

    public static void cacheTest(){
        CacheUtil.put("manual-cache","user",new User());
        CacheUtil.get("manual-cache","user");
    }

    public static Component getComponent(){
        Session session = null;
        Transaction ax = null;
        Session session2 = null;
        Transaction ax2 = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();
            Component component = session.get(Component.class,1);

            session2 = HibernateUtil.getSession();
            ax2 = session2.beginTransaction();
            Component component2 = session2.get(Component.class,1);

            component.getName().setFirstName("new name");
            component2.getName().setFirstName("new2 name");

            System.out.println("版本 "+component2.getVersion());
            ax.commit();
            ax2.commit();
            return component;
        } catch (Exception e) {

            // 以上代码不会产生异常
            if(ax != null){
                System.out.println("1 ======== 版本回滚!");
                ax.rollback();
            }
            if(ax2 != null){
                System.out.println("2 ======== 版本回滚!");
                ax2.rollback();
            }
            e.printStackTrace();
            System.out.println("======== "+e.getMessage());
        }finally {
            if(session != null){
                session.close();
            }
            if(session2 != null){
                session2.close();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        //System.out.println(getUser().getName());
        //System.out.println(getUser().getName());

        //System.out.println(getComponent().getName().getFirstName());
        System.out.println("版本 "+getComponent().getVersion());

        // 输出 cache 的统计信息
        Statistics st = HibernateUtil.getSessionFactory().getStatistics();
        System.out.println(st);
        // 输出二级缓存 cache 的命中次数
        System.out.println("命中次数: "+st.getSecondLevelCacheHitCount());
        System.out.println("未命中次数: "+st.getSecondLevelCacheMissCount());
        System.out.println("放入缓存次数: "+st.getSecondLevelCachePutCount());

    }
}
