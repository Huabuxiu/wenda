package com.example.wenda.dao;

import com.example.wenda.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;



@Mapper
@Component
public interface MessageDAO {

    String TABLE_NAME = "message ";
    String INSERT_FILEDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FILEDS = " id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FILEDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
   int addMessage(Message message);


    @Select({"select ", SELECT_FILEDS ," from ", TABLE_NAME, " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);


//    select  *,count(id) as cnt from
//                              (select * from message where to_id = 14 or from_id = 14) tt
//    group by conversation_id
//    order by created_date desc;

    @Select({"select ",INSERT_FILEDS," ,count(id) as cnt from (select * from ",TABLE_NAME," where to_id = #{userId}  or from_id = #{userId}) " +
            "tt group by conversation_id order by created_date desc  limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConvesationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

//    update message set has_read = 1 where conversation_id = "11_14" and  to_id = 14;
    @Update({"update ",TABLE_NAME," set has_read = 1 where to_id=#{userId} and conversation_id=#{conversationId}"})
    void updateConvesationUnread(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
