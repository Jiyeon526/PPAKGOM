package com.ppakgom.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.ppakgom.chat.dao.ChatMessage;

@RestController
@CrossOrigin("*")
public class ChatMessageController {
	private final SimpMessagingTemplate template;

    @Autowired
    public ChatMessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

//    @MessageMapping("/{conference_id}/join")
//    public void join(@PathVariable Long conference_id, ChatMessage message) {
//        message.setMessage(message.getWriter() + "님이 입장하셨습니다.");
//        template.convertAndSend("/subscribe/" + Long.toString(message.getConference_id()), message);
//    }
    
    @MessageMapping("/conferences/join")
    public void join(ChatMessage message) {
        message.setMessage(message.getWriter() + "님이 입장하셨습니다.");
        System.out.println("/chat/join");
        template.convertAndSend("/subscribe/conferences/" + Long.toString(message.getConference_id()), message);
    }

//    @MessageMapping("/{conference_id}/send")
//    public void message(@PathVariable Long conference_id, ChatMessage message) {
//        template.convertAndSend("/subscribe/" + Long.toString(message.getConference_id()), message);
//    }
    
    @MessageMapping("/conferences/send")
    public void message(ChatMessage message) {
    	System.out.println("/chat/send");
        template.convertAndSend("/subscribe/conferences/" + Long.toString(message.getConference_id()), message);
    }
}