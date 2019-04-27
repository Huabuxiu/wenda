package com.example.wenda.service;

import com.example.wenda.dao.CommentDAO;
import com.example.wenda.dao.QuestionDAO;
import com.example.wenda.model.Comment;
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
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDAO.getCommentByEntity(entityId,entityType);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    //更改成状态1为不可见
    public void deleteComment(int entityId, int entityType) {
        commentDAO.updateStatus(entityId, entityType, 1);
    }

}
