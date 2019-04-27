package com.example.wenda.dao;

import com.example.wenda.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface QuestionDAO {

    String TABLE_NAME = "question ";
    String INSERT_FILEDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FILEDS = " id, "+INSERT_FILEDS;

    @Insert({"insert into question (",INSERT_FILEDS,") values" +
            " (#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);

    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAME," where id = #{qid}"})
    Question selectQuestionByid(int qid);

    List<Question> selectLatestQuestions(@Param("userId") int userid,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);


    //更新评论数
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

}
