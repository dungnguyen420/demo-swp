package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "class_members")
@Data
public class ClassMember {


        @EmbeddedId
        private ClassMemberId id;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("classId")
        @JoinColumn(name = "class_id")
        private ClassesEntity classEntity;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("userId")
        @JoinColumn(name = "user_id")
        private UserEntity member;

}
