package com.example.wenda.dao;

import com.example.wenda.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    List<Question> selectLatestQuestions(@Param("userId") int userid,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

}
