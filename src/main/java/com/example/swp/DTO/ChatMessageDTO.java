package com.example.swp.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    private Long id;
    private String from;
    private String to;
    private String content;
    private LocalDateTime timestamp;
}
