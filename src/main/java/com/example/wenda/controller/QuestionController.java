package com.example.wenda.controller;

import com.example.wenda.model.*;
import com.example.wenda.service.*;
import com.example.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description:问题提交页面 ajax
 * @author: Huabuxiu
 * @create: 2019-03-27 09:02
 **/
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;



    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
            try {
                Question question = new Question();
                question.setContent(content);
                question.setTitle(title);
                question.setCreatedDate(new Date());
                if (hostHolder.getUser()==null){
                    question.setUserId(WendaUtil.ANONYMOUS_USERID);
//                    return WendaUtil.getJSONString(999);
                }else {
                    question.setUserId(hostHolder.getUser().getId());
                }
                if (questionService.addQuestion(question)>0){
                    return WendaUtil.getJSONString(0);
                }
            }catch (Exception e){
                logger.error("增加题目失败"+e.getMessage());
            }
        return WendaUtil.getJSONString(1, "失败");
    }


    @RequestMapping(path = {"/question/{qid}"})
    public String detail(Model model, @PathVariable("qid") int qid){
        Question question = questionService.getById(qid);

        model.addAttribute("question",question);
        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();

        for (Comment comment:
                commentList)
        {
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }

            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));

            vo.set("user",userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments",vos);

        List<ViewObject> followUsers = new ArrayList<>();
        //获取关注的用户信息

        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION,qid,20);

        for (Integer userId :
                users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);

            if (u == null){
                continue;
            }
            vo.set("name",u.getName());
            vo.set("headUrl",u.getHeadUrl());
            vo.set("id",u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers",followUsers);
        if (hostHolder.getUser()!=null){
            model.addAttribute("followed",
                    followService.IsFollower(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,qid));
        }else {
            model.addAttribute("followed",false);
        }
        return "detail";
    }
}
