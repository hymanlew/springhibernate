package com.hyman.demo;

import com.hyman.entity.Department;
import com.hyman.entity.Emploee;
import com.hyman.entity.IdCard;
import com.hyman.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;

public class Many2one {

    public static Department addDepartment(){
        Session session = null;
        Transaction ax = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();

            Department depart = new Department();
            depart.setName("技术开发");
            //Department depart = session.get(Department.class,1);

            Emploee emploee = new Emploee();
            emploee.setName("小王");
            // 对象模型，建立两个对象之间的联系
            emploee.setDepartment(depart);

            Emploee emploee1 = new Emploee();
            emploee1.setName("小张");
            emploee1.setDepartment(depart);

            // 如果外键有非空约束，就要注意保存的顺序了，否则会抛异常（当设置为 true维护关联关系，并且没有设置级联操作时才会）。
            session.saveOrUpdate(depart);
            session.saveOrUpdate(emploee);
            session.saveOrUpdate(emploee1);

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

    public static Emploee addEmpl(){
        Session session = null;
        Transaction ax = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();

            Emploee emploee = new Emploee();
            emploee.setName("小王");
            Emploee emploee1 = new Emploee();
            emploee1.setName("小张");
            Set<Emploee> emploees = new HashSet<>();
            emploees.add(emploee);
            emploees.add(emploee1);

            Department depart = new Department();
            depart.setName("技术开发二");
            // 对象模型，建立两个对象之间的联系
            depart.setEmploees(emploees);

            /**
             * 如果外键有非空约束，就要注意保存的顺序了，否则会抛异常。
             * 因为 hibernate默认对复杂的属性（对象）是不会自动进行 sql 操作的。如果需要它自动去执行，则要在配置文件中指定其
             * 级联操作，cascade="save-update"。
             */
            //session.saveOrUpdate(emploee);
            //session.saveOrUpdate(emploee1);
            session.saveOrUpdate(depart);
            ax.commit();

            /**
             * 要特别注意，这一行代码会抛 classcast 类型转换异常。虽然我们声明的是 hashset，但 hibernate 在运行时会处理为
             * PersistentSet 类型（即 hibernate 对 set 的实现类）。所以它就会转换失败。
             * 因为 hibernate 为了实现懒加载的功能，它对 java所有的集合类都进行了覆写（添加了懒加载的功能）。所以在类中声明
             * 集合类型时，也只能用接口声明，不能使用其实现类（例如 hashset）。
             */
            //HashSet set = (HashSet) depart.getEmploees();
            return emploee;
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

    public static Emploee getEmpl(){
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            Emploee emploee = session.get(Emploee.class,1);

            //System.out.println(emploee.getDepartment().getName());
            Hibernate.initialize(emploee.getDepartment());

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

    public static Department getDepartment(){
        Session session = null;
        try {
            session = HibernateUtil.getSession();

            Department depart = session.get(Department.class,1);
            Hibernate.initialize(depart.getEmploees());

            return depart;
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

    public static Emploee addCard(){
        Session session = null;
        Transaction ax = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();

            IdCard card = new IdCard();
            card.setIdententy("123456");
            Emploee emploee = session.get(Emploee.class,1);
            emploee.setCard(card);
            card.setEmploee(emploee);

            session.saveOrUpdate(card);
            session.saveOrUpdate(emploee);

            ax.commit();
            Hibernate.initialize(emploee.getCard());
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

    public static void main(String[] args) {
        //System.out.println(addDepartment());

        // LazyInitializationException，session 已经关闭了
        System.out.println(getEmpl().getDepartment().getName());

        //Department department = getDepartment();
        //System.out.println(department.getEmploees().size());

        //System.out.println(addCard().getCard().getIdententy());

        //System.out.println(getEmpl().getCard().getIdententy());
    }
}
