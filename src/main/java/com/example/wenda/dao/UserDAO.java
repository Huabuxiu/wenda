package com.example.wenda.dao;

import com.example.wenda.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDAO {

    String TABLE_NAME = " user ";
    String INSERT_FILEDS = " name, password, salt, head_url ";
    String SELECT_FILEDS = " id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FILEDS,
            ") values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);
}
