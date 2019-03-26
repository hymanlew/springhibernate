package com.hyman.entity;

/**
 * 由 hibernate 管理的 javabean 的规则：
 * 必须有一个无参的构造方法。该类必须是非 final 的（否则会影响到懒加载）。最好有主键 id。
 */
public class User {

    private Integer userId;
    //private String version;
    private String name;
    private String password;

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    //public String getVersion() {
    //    return version;
    //}
    //
    //public void setVersion(String version) {
    //    this.version = version;
    //}

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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
