package com.example.wenda.model;

import org.springframework.stereotype.Component;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-26 21:24
 **/
@Component
public class HostHolder {

    //使用全局的线程都可以看到
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}
