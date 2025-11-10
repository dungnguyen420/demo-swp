package com.example.swp.Service.impl;

import com.example.swp.DTO.ChatMessageDTO;
import com.example.swp.DTO.UserDTO;
import com.example.swp.Entity.ChatMessageEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Repository.ChatMessageRepository;
import com.example.swp.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepo;
    private final ModelMapper modelMapper;
    private final IUserRepository userRepo;


    public ChatMessageDTO saveMessage(ChatMessageDTO chatMessageDTO) {

        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSenderUsername(chatMessageDTO.getFrom());
        entity.setRecipientUsername(chatMessageDTO.getTo());
        entity.setContent(chatMessageDTO.getContent());
        entity.setTimestamp(LocalDateTime.now());

        ChatMessageEntity savedEntity = chatMessageRepo.save(entity);

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(savedEntity.getId());
        dto.setFrom(savedEntity.getSenderUsername());
        dto.setTo(savedEntity.getRecipientUsername());
        dto.setContent(savedEntity.getContent());
        dto.setTimestamp(savedEntity.getTimestamp());

        return dto;
    }


    public List<ChatMessageDTO> getHistory(String user1, String user2) {
        List<ChatMessageEntity> messages = chatMessageRepo
                .findBySenderUsernameAndRecipientUsernameOrRecipientUsernameAndSenderUsernameOrderByTimestampAsc(
                        user1, user2, user2, user1);


     return messages.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }
    public List<UserDTO> getContacts(String managerUsername) {


        List<String> usernames = chatMessageRepo.findUsersWhoChattedWithManager(managerUsername);


        return usernames.stream()
                .map(username -> {

                    UserEntity user = userRepo.findByUserName(username).orElse(null);
                    if (user != null) {

                        return modelMapper.map(user, UserDTO.class);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    private ChatMessageDTO convertEntityToDTO(ChatMessageEntity entity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(entity.getId());
        dto.setFrom(entity.getSenderUsername()); // Sửa lỗi "null"
        dto.setTo(entity.getRecipientUsername());
        dto.setContent(entity.getContent());
        dto.setTimestamp(entity.getTimestamp());
        return dto;
    }
}
