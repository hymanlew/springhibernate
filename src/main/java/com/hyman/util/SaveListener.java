package com.hyman.util;

import com.hyman.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

/**
 * 拦截器与事件都是 hibernate 的扩展机制，但现在它默认已经从 interceptor改为事件监听机制了。这两个都是 hibernate 的回调接口，hi
 * 在 save,delete,update 等操作时都会回调这些类。
 * 需要在 hibernate 配置文件中配置 event 事件监听器，并且必须要与 default 默认的监听器一起使用，因为它内部会实现真正的数据库操
 * 作。否则就只会执行自定义的监听操作而不会执行操作。
 *
 * 注意事件监听器在执行时，是有先后顺序的（即在配置文件的上下顺序）。
 */
public class SaveListener implements SaveOrUpdateEventListener{

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent saveOrUpdateEvent) throws HibernateException {
        if(saveOrUpdateEvent.getObject() instanceof User){
            User user = (User) saveOrUpdateEvent.getObject();
            System.out.println("事件监听器："+user.getName());
        }
    }
}
