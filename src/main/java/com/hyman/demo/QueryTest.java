package com.hyman.demo;

import com.hyman.entity.User;
import com.hyman.util.HibernateUtil;
import javafx.scene.transform.Rotate;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

/**
 * HQL（Hibernate query language），它是面向对象的查询语言（而 sql 是面向表的查询）。HQL中的对象名是区分大小写的（除了
 * java类和属性不区分大小写），它查的是对象而不是表，并且支持多态。它主要通过 query 来实现复杂的操作。
 */
public class QueryTest {

    // hql 多条件查询（推荐）。
    public static void test1(){
        Session session = null;
        try {
            session = HibernateUtil.getSession();

            /**
             * createSQLQuery 是指用原生的 sql 语句来执行，hibernate不会自动生成。并且如果需要自动封装到对象的话，还必
             * 须 addEntity(Obj.class)，否则不会自动封装。
             */
            //String sql = "SELECT * FROM user WHERE name LIKE ?";
            //Query query = session.createSQLQuery(sql).addEntity(User.class);
            // createSQLQuery 参数下标与 JDBC 一样都是从 1 开始的。
            //query.setParameter(1,"hyman");

            // HQL（Hibernate query language），是面向对象的，所以这里必须使用 User 对象。并且参数占位符不能直接是？，
            // 因为高版本的 hibernate已不再支持。？后面需要加上下标位置。
            //String sql = "from User as user where user.name=?0";
            //Query query = session.createQuery(sql);
            // createQuery 参数下标是从 0 开始的，而 JDBC 是从 1 开始的
            //query.setParameter(0,"hyman");

            String sql = "from User as user where user.name=:name";
            Query query = session.createQuery(sql);
            query.setParameter("name","hyman");


            // 实现分页的功能，从第几条数据开始，要拿到多少条。它是 hibernate封装的方法，所以可用于任何数据库分页。
            // 并且是基于方言的，即连接 mysql 就用于 mysql。连接 oracle 就用于 oracle。
            query.setFirstResult(0);
            query.setMaxResults(10);

            // 如果确定查询的结果只有一条时，也可以使用这个方法。
            //query.uniqueResult();

            List list = query.list();
            for(Iterator ite = list.iterator(); ite.hasNext();){
                System.out.println("1 === "+ite.next());
            }

        } catch (Exception e) {
            // 如果此代码是在 DAO 层执行时，通常要把异常抛出给调用层
            e.printStackTrace();
            System.out.println("======== "+e.getMessage());
        }finally {
            // 关闭连接
            if(session != null){
                session.close();
            }
        }
    }

    // CriteriaQuery 多条件查询，它比 hql 更面向对象，但是使用不方便。
    public static void test2(){
        Session session = null;
        try {
            session = HibernateUtil.getSession();

            // 注意导入的包是 import javax.persistence.criteria.CriteriaQuery;
            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> ciquery = cBuilder.createQuery(User.class);

            // 指定根条件
            Root<User> root = ciquery.from(User.class);
            ciquery.where(cBuilder.like(root.<String>get("name"),"hyman"));


            // 条件查询："name"是实体类的属性名称。Predicate 是条件对象。
            // isNull 为空，isNotNull 非空
            Predicate p1 = cBuilder.isNull(root.get("password"));
            // Equal 相等，notEqual 不相等
            Predicate p2 = cBuilder.notEqual(root.get("password"),"123");

            // in 查询，not in 查询
            CriteriaBuilder.In in = cBuilder.in(root.get("password"));
            cBuilder.not(in);

            // 日期区间比较
            cBuilder.between(root.<Date>get("date"),new Date(),new Date());
            // lessThan 小于某个日期，greaterThan 大于某个日期
            cBuilder.lessThan(root.<Comparable>get("date"),new Date());

            // le 小于等于， lt 小于
            cBuilder.le(root.<Number>get("id"),10);
            // ge 大于等于， gt 大于
            cBuilder.ge(root.<Number>get("id"),10);

             // asc 升序， desc 降序
             cBuilder.asc(root.get("id"));

            // and，or 条件查询
            cBuilder.and(p1,p2);
            cBuilder.or(p1,p2);
            Path<Date> datePath=root.get("workDay");

            ciquery.where(p1);


            Query query = session.createQuery(ciquery);
            List<User> list = query.getResultList();
            for(User user : list){
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        test1();
        //test2();
    }
}
