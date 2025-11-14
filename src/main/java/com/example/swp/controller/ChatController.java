package com.example.swp.Controller;

import com.example.swp.DTO.ChatMessageDTO;
import com.example.swp.Service.impl.ChatService;
import com.example.swp.Service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    @GetMapping("/chat/admin")
    public String openMemberChatWithAdmin(Model model,
                                          @AuthenticationPrincipal CustomUserDetails principal) {
        String currentUser = principal.getUsername();
        String adminUsername = "manager_admin";

        model.addAttribute("recipientName", "Manager");
        model.addAttribute("recipientUsername", adminUsername);
        model.addAttribute("chatHistory",
                chatService.getHistory(currentUser, adminUsername));

        return "chat/manager-chat-page";
    }
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage, Principal principal) {


        try {
            String senderUsername = principal.getName();
            chatMessage.setFrom(senderUsername);
            chatMessage.setTo("admin");
            if (chatMessage.getTo() == null || chatMessage.getTo().trim().isEmpty()) {
                System.err.println("CHAT_DEBUG_ERROR: Recipient (To) is NULL or EMPTY.");
                return;
            }

            ChatMessageDTO savedMessage = chatService.saveMessage(chatMessage);
            String recipientUsername = savedMessage.getTo();

            messagingTemplate.convertAndSendToUser(
                    recipientUsername,
                    "/queue/reply",
                    savedMessage
            );

            messagingTemplate.convertAndSendToUser(
                    senderUsername,
                    "/queue/reply",
                    savedMessage
            );

        } catch (Exception e) {
            // ⭐️⭐️⭐️ QUAN TRỌNG NHẤT ⭐️⭐️⭐️
            // In LỖI GỐC ra console.
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("!!!       ĐÂY LÀ LỖI CHAT GỐC       !!!");
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace(); // In ra toàn bộ lỗi gốc
            // ⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️
        }
    }
}