package com.hyman.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.*;
import java.io.IOException;

// 模拟 OpenSessionInViewFilter 和 current_session_context_class=thread 的原理，绑定 session 与 request 请求解决懒加载的问题。
public class OpenSessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Session session = null;
        Transaction ax = null;

        // 在一次 request 请求中只使用一个 session。
        try {
            session = HibernateUtil.getThreadLocalSession();
            ax = session.beginTransaction();
            filterChain.doFilter(servletRequest,servletResponse);

            ax.commit();
        }catch (Exception e){
            ax.rollback();
            throw new RuntimeException("事务回滚");
        }finally {
            HibernateUtil.closeSession();
        }

        // 然后使用时直接调用 session 操作即可。在一次 request 请求中只使用一个 session，简化了代码和 session 操作。
        //static void addObject(Object obj){
        //    HibernateUtil.getSession().saveOrUpdate(obj);
        //}
    }

    @Override
    public void destroy() {

    }
}
