package com.example.wenda.service;

import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-25 20:31
 **/
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    public User getUser(int id ){
        return userDAO.selectUserById(id);
    }
}
