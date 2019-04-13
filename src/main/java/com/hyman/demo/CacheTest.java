package com.hyman.demo;

import com.hyman.entity.*;
import com.hyman.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CacheTest {

    public static Component getComponent(){
        Session session = null;
        Transaction ax = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();

            Name name = new Name("xiao","zhang");
            Component component = new Component();
            component.setName(name);

            ax.commit();
            return component;
        } catch (Exception e) {

            if(ax != null){
                ax.rollback();
            }
            e.printStackTrace();
            System.out.println("======== "+e.getMessage());
        }finally {
            if(session != null){
                session.close();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        //System.out.println(getRole().getRoles().size());

    }
}
