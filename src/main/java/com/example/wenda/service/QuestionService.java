package com.example.wenda.service;

import com.example.wenda.dao.QuestionDAO;
import com.example.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-25 20:34
 **/
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public int addQuestion(Question question){
        return questionDAO.addQuestion(question) > 0 ? question.getId(): 0;
    }
    public Question getById(int id){
        return  questionDAO.selectQuestionByid(id);
    }

}
