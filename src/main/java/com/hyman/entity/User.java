package com.hyman.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

/**
 * 由 hibernate 管理的 javabean 的规则：
 * 必须有一个无参的构造方法。该类必须是非 final 的（否则会影响到懒加载）。最好有主键 id。
 */

/**
 * 使用 hibernate 的 ehcache 缓存注解，但要注意当有配置文件时，要在配置文件中设置，否则不起作用。即注解与配置文件不可共存。
 * region：指定缓存的空间（可选）。
 * include：all 包含所有属性，non-lazy 仅包含非延迟加载的属性（可选）。
 *
 *  @Entity 注解是 JPA herbinate 必须要有的，作用是声明一个实体类并与数据库表映射。也可以指定一个表映射 @Table。不写默认就是当前类名的小写，即 user。
 *
 * hibernate 会给每个被管理的 pojo 加入一个 hibernateLazyInitializer属性（并且 struts-jsonplugin 或者其他的 jsonplugin 都是）。而且 jsonplugin
 * 用的是 java的内审机制，jsonplugin 通过 java 的反射机制将 pojo 转换成 json，会把 hibernateLazyInitializer 也拿出来操作,但是 hibernateLazyInitializer
 * 无法由反射得到，所以就抛异常了。
 *
 * @JsonIgnoreProperties(value={“xxx”}) 注解是必须要加在 pojo 类上的，value 值就是要忽略的一些属性，这些属性是被 lazy加载的，也就是many-to-one的 one
 * 端的 pojo上。
 * 以下注解的作用是告诉 jsonplug 组件，在将代理对象转换为 json 对象时，忽略value对应的数组中的属性，即：通过 java的反射机制将 pojo转换成 json的，属性，
 * 通过 java的反射机制将 pojo转换成 json的控制器。
 *
 * 如果你想在转换的时候继续忽略其他属性，可以在数组中继续加入。
 */

//@Cache(
//        usage =  CacheConcurrencyStrategy.READ_WRITE,
//        region = "manual-cache",
//        include = "all"
//        )
//@Entity
//@Table(name = "user")
//@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class User {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    //@Column
    private String name;
    private String password;

    //@JoinColumn(name = "user_id")
    //@ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
    private Set<Role> roles;

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
