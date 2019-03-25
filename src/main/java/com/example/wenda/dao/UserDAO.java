package com.example.wenda.dao;

import com.example.wenda.model.User;
import org.apache.ibatis.annotations.*;
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


    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAME," where id = #{id}"})
    User SelectUserById(int id);

    @Update({"update ",TABLE_NAME," set password = #{password} where id =#{id}"})
    void  updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

}