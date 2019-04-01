package com.example.wenda.dao;

import com.example.wenda.model.Comment;
import com.example.wenda.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CommentDAO {

    String TABLE_NAME = "comment ";
    String INSERT_FILEDS = " user_id, content, created_date, entity_id, entity_type, status";
    String SELECT_FILEDS = " id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAME," (",INSERT_FILEDS,") values",
            " (#{userId}, #{content}, #{createdDate}, #{entityId}, #{entityType},#{status})"})
    int addComment(Comment comment);



    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAME,
                " where entity_id = #{entityId} and entity_type=#{entityType}"})
    List<Comment> getCommentByEntity(@Param("entityId") int entityId,
                                         @Param("entityType")int entityType);

}
