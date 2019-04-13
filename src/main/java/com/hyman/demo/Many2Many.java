package com.hyman.demo;

import com.hyman.entity.*;
import com.hyman.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;

public class Many2Many {

    public static User addUser(){
        Session session = null;
        Transaction ax = null;
        try {
            session = HibernateUtil.getSession();
            ax = session.beginTransaction();

            User u1 = new User();
            u1.setName("u1");
            u1.setPassword("111");

            User u2 = new User();
            u2.setName("u2");
            u2.setPassword("222");

            Role r1 = new Role();
            r1.setName("teacher");

            Role r2 = new Role();
            r2.setName("admin");

            Set<User> users = new HashSet<>();
            users.add(u2);
            Set<Role> roles = new HashSet<>();
            roles.add(r1);
            roles.add(r2);

            // u1 用户同时拥有两个权限
            u1.setRoles(roles);
            // u2 用户只拥有 teacher 权限
            r1.setUsers(users);
            // 这里需要注意一下，在 user与 role相互填充数据的同时，不可以重复，否则会抛异常。因为 hibernate 不能对相同的
            // 操作执行 insert两次，会造成主键冲突。（也因为多对多情况下 inverse 默认也都是 false，即都是需要维护关联关系的）。
            // 而在一对多的关系中，之所以可以执行，因为它只是进行了外键的更新，而不是进行 insert插入操作。

            session.saveOrUpdate(u1);
            session.saveOrUpdate(u2);
            session.saveOrUpdate(r1);
            session.saveOrUpdate(r2);
            ax.commit();
            return u1;
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

    public static User getRole(){
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            User user = session.get(User.class,1);
            Hibernate.initialize(user.getRoles());

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

    public static void main(String[] args) {
        //addUser();

        System.out.println(getRole().getRoles().size());

    }
}
