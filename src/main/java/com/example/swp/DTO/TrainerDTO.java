package com.example.swp.DTO;

import lombok.Data;

@Data
public class TrainerDTO {
    private long id;
    private String email;
    private String passWord;
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
    private String certificationInfo;

}
