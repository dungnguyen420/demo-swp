package com.example.swp.Controller;

import com.example.swp.DTO.ChatMessageDTO;
import com.example.swp.DTO.UserDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.impl.ChatService;
import com.example.swp.Service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatPageController {

    private final ChatService chatService;
    private final UserService userService;


    private final String ACTUAL_MANAGER_USERNAME = "manager";

    @GetMapping("/chat")
    public String getChatPage(Model model, Principal principal) {


        UserEntity manager = userService.findByUserName(ACTUAL_MANAGER_USERNAME);

        if (manager == null) {

            return "redirect:/home?error=ManagerAccountNotFound";
        }

        String managerUsername = manager.getUserName();
        String currentUsername = principal.getName();

        List<ChatMessageDTO> history = chatService.getHistory(currentUsername, managerUsername);

        model.addAttribute("chatHistory", history);
        model.addAttribute("recipientUsername", managerUsername);


        model.addAttribute("recipientName", manager.getUserName());

        return "chat/chat-page";
    }


    @GetMapping("/admin/chat")
    public String getManagerInbox(Model model, Principal principal) {

        String managerUsername = principal.getName();
        List<UserDTO> contacts = chatService.getContacts(managerUsername);

        model.addAttribute("contacts", contacts);
        model.addAttribute("isInbox", true);

        return "chat/manager-chat-page";
    }



    @GetMapping("/admin/chat/{username}")
    public String getManagerChatWithUser(
            @PathVariable String username,
            Model model,
            Principal principal) {

        String managerUsername = principal.getName();

    
        UserEntity selectedUser = userService.findByUserName(username);
        if (selectedUser == null) {
            return "redirect:/admin/chat?error=UserNotFound";
        }

        List<UserDTO> contacts = chatService.getContacts(managerUsername);
        List<ChatMessageDTO> history = chatService.getHistory(managerUsername, username);

        model.addAttribute("contacts", contacts);
        model.addAttribute("chatHistory", history);
        model.addAttribute("selectedUser", selectedUser);
        model.addAttribute("isInbox", false);

        return "chat/manager-chat-page";
    }
}