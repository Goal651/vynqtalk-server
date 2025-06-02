package com.vynqtalk.server.model.sockets;


import com.vynqtalk.server.model.Group;
import com.vynqtalk.server.model.GroupMessage;
import com.vynqtalk.server.model.User;

import lombok.Data;

@Data
public class ChatGroupMessageReply  {
    private User sender;
    private Group group;
    private String content;
    private String type; 
    private GroupMessage replyToMessage;
}

