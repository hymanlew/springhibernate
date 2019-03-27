package com.hyman.demo;

import com.hyman.entity.Department;
import com.hyman.entity.Emploee;
import com.hyman.entity.User;
import com.hyman.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Many2one {

    public static Department addDepartment(){
        Session session = null;
        Transaction ax = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();

            Department depart = new Department();
            depart.setName("技术开发");
            Emploee emploee = new Emploee();
            emploee.setName("小王");
            emploee.setDepartment(depart);

            session.saveOrUpdate(depart);
            session.saveOrUpdate(emploee);
            ax.commit();

            return depart;
        } catch (Exception e) {
            if(ax != null){
                ax.rollback();
            }
            e.printStackTrace();
            System.out.println("======== "+e.getMessage());
        }finally {
            // 关闭连接
            if(session != null){
                session.close();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(addDepartment());
    }
}
