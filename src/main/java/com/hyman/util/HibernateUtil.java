package com.hyman.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;

// 定义工具类，不可创建实例，不可被更改

/**
 * Hibernate 有五个核心接口：
 * 1，Configuration：负责加载主配置文件信息，同时也加载映射关系文件信息。
 * 2，SessionFactory：负责创建 session对象。
 * 3，session：数据库连接会话，负责执行增删改操作。
 * 4，transaction：负责事务控制。
 * 5，query：负责执行特殊查询。
 *
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

    private static ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
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

    /**
     * Session 是非线程安全的，生命周期较短，代表一个数据库连接。在B/S系统中一般不会超过一个请求，其内部维护一级缓存和数据库连
     * 接。如果 session 长时间会占用大量内存和数据库连接。
     * SessionFactory 是线程安全的，一个数据库对应一个 factory，生命周期长，一般在整个系统生命周期内有效。它保存着和数据库连接
     * 的相关信息（name，password，url）和映射信息，以及 hibernate 运行时要用到的一些信息。
     * @return
     */
    public static Session getSession() {
        // 打开一个新的 Session
        return sessionFactory.openSession();
    }

    /**
     * getThreadLocalSession() 方法和 closeSession() 方法的运行原理就是 sessionFactory.getCurrentSession() 的原理。
     *
     * 所以在实际使用 session 的线程安全或是保证整个程序流程中都使用同一个 session 时，可以直接使用 sessionFactory.getCurrentSession()
     * 方法即可，并且该方法产生的 session 在 commit 或 rollback 之后会自动关闭，而不需要手动关闭。
     * 注意使用 CurrentSession 需要在 hibernate 配置文件中配置 CurrentSessionContext。
     * @return
     */
    public static Session getThreadLocalSession(){
        Session session = sessionThreadLocal.get();
        if(session != null){
            return session;
        }
        session = getSession();
        sessionThreadLocal.set(session);
        return session;
    }

    public static void closeSession(){
        Session session = sessionThreadLocal.get();
        if(session != null){
            session.close();
            sessionThreadLocal.remove();
        }
    }

    public static void batchSave(){
        Session session = getSession();
        for (int i=0; i<100000; i++) {
            session.save(new Object());

            // 在循环对 session 进行操作时，要注意清空缓存，否则会造成 OOM。并且 clear之前要先 flush对数据库进行最新的更新。
            // 但要注意 flush 方法其他情况下不需要手动调用，因为它会耗费大量的资源。
            if (i % 50 == 0) {
                session.flush();
                session.clear();
            }
        }

        /**
         * 像这种类似的业务中需要进行大批量的数据操作时，直接使用 session是非常麻烦的（因为它会一直保持 session中的状态，瞬时，
         * 持久，托管）。
         * 所以 hibernate提供了一个专门的大批量操作接口，无状态的 session。它不和一级二级缓存交互，也不触发任何事件，监听器拦截
         * 器，该接口会立即发送数据到数据库，与 JDBC 功能一样。所以该方法也就没有 flush，clear 的必要了。调用的方法与 session 类似。
         *
         * 另外 query.executeUpdate() 方法进行批量更新会清空一二级缓存中的数据，也不会对操作对象的关联对象版本号进行同步的实时
         * 更新，这就容易造成关联不准确或失效。并且也可能会造成级联。
         */
        StatelessSession statelessSession = sessionFactory.openStatelessSession();
    }
}
