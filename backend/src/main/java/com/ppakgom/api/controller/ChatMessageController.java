package com.ppakgom.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.ppakgom.chat.dao.ChatMessage;

@Controller
public class ChatMessageController {
	private final SimpMessagingTemplate template;

    @Autowired
    public ChatMessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

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
