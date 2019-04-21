package com.hyman.demo;

import com.hyman.entity.User;
import com.hyman.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SessionTest {

    public static void test1(){
        Session session = null;
        Transaction ax = null;

        try {
            session = HibernateUtil.getSession();
            // 手动开启事务
            ax = session.beginTransaction();

            User user = new User();
            user.setName("lili");

            session.delete(user);
            session.update(user);
            // 保存或是更新
            session.saveOrUpdate(user);
            // 根据主键获取对象
            user = session.get(User.class,user.getUserId());

            /**
             * 该方法与 get 的区别是，get 方法会立即访问数据库执行。而 load 是懒加载执行的，不会立即访问数据库。即
             * 只有当下文中有用到 user 对象时它才会执行。如果没有使用 user对象，则它不会执行（也没有输出 sql 语句），
             * 并且会抛出异常（LazyInitializationException）。
             * 并且 load 方法返回的对象永远都不可能是 null 的，即使传入的主键是错误的。如果是错误的主键，则它会自动生
             * 成一个当前对象的子类（即继承于 User），来返回出来。这也是为什么 User 类不能声明为 final 的原因。
             */
            user = session.load(User.class,user.getUserId());
            System.out.println(user.getName());

            /**
             * 该方法与 save 的区别是，当没有开启事务时，它就会直接不执行插入操作。而 save 会先插入数据然后再回滚删除。
             * 如果已经开启了事务，则这两个方法的作用是一样的，都是插入数据。
             */
            session.persist(user);
            session.save(user);
            ax.commit();
        } catch (Exception e) {
            if(ax != null){
                ax.rollback();
            }
            // 如果此代码是在 DAO 层执行时，通常要把异常抛出给调用层
            e.printStackTrace();
            System.out.println("======== "+e.getMessage());
        }finally {
            // 关闭Session，释放资源(不一定是真正的关闭)
            if(session != null){
                session.close();
            }
        }
    }

    public static void test2(){
        /**
         * session 所具有的方法：
         * beginTransaction()：  手动开启事务，操作 commit，或是回滚 rollback。
         * clear()：             清除缓存。
         * contains()：          是否包含某对象。
         * flush()：             让 session 与数据库进行数据同步。
         * lock()：              持久化对象，但不会同步对象的状态，即不会自动 update。
         *
         * session 内部封装了 connection 数据库的连接，并且 hibernate对连接进行了很多优化。所以不能直接拿到连接，只能
         * 重新连接（reconnect），和断开连接（disconnect）。
         *
         * session 中对象的状态：
         * 瞬时（transient）：数据库中没有数据与之对应，超过作用域会被 JVM回收，一般是刚刚 new出来且与当前 session没有
         * 关联的对象。此时对象的状态就是瞬时。
         * 对应的方法：new()，get()，load()，find()，iterator()，delete()。
         *
         * 持久（persistent）：数据库中有数据与之对应（如果没有数据，且 session也没有提交，则还是瞬时的状态），并且与当
         * 前 session有关联，且相关联的 session没有关闭，事务没有提交时，session 中的对象就是处于持久状态。当持久对象状
         * 态发生改变，在事务提交时会影响到数据库（hibernate 能检测到，即先 insert，后 update）。
         * 对应的方法：save()，saveOrUpdate()，update()，lock()。
         *
         * 脱管（detached）：数据库中有数据与之对应（session 事务已经提交），但当前没有 session与之关联时（即 session已
         * 经关闭后），此时的对象就是处于session 的脱管状态。脱管对象状态发生改变，hibernate 不能检测到。
         * 对应的方法：close()，clear()。
         */

    }

    public static User test3(){
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSession();
            user = session.load(User.class,1);
            // 初始化对象
            //Hibernate.initialize(user);

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
        return user;
    }

    public static void main(String[] args) {
        User user = test3();
        // 抛出懒加载异常，LazyInitializationException，并且它跟是否有提交事务是没有关系的。关键在于懒加载。
        System.out.println(user.getName());
    }
}
