package com.hyman.demo;

import com.hyman.entity.*;
import com.hyman.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;

public class SubTest {

    public static Emploee addEmpl(){
        Session session = null;
        Transaction ax = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();

            Department depart = new Department();
            depart.setName("综合部门");

            Emploee emploee = new Emploee();
            emploee.setName("普通员工");
            emploee.setDepartment(depart);

            Technical th = new Technical();
            th.setName("技术小王");
            th.setTechnology("java");
            th.setDepartment(depart);

            Sale sale = new Sale();
            sale.setName("销售小张");
            sale.setSales(1000.0);
            sale.setDepartment(depart);

            session.saveOrUpdate(emploee);
            session.saveOrUpdate(th);
            session.saveOrUpdate(sale);
            ax.commit();
            return emploee;
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

    public static Emploee getEmpl(){
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            Emploee emploee = session.get(Emploee.class,6);

            // 此时 hibernate的查询就是多态的查询，即它会准确地查出子类对象。但它是使用外连接去查询所以的子类表，所以当子类很
            // 多时查询性能会很低。所以这就需要在查询时，明确指定子类的类型（子类.class）。
            System.out.println(emploee.getClass());

            return emploee;
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

    public static void main(String[] args) {

        //addEmpl();
        getEmpl();
        //System.out.println(getRole().getRoles().size());

    }
}
