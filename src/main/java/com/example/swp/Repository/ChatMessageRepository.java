package com.example.swp.Repository;

import com.example.swp.Entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findBySenderUsernameAndRecipientUsernameOrRecipientUsernameAndSenderUsernameOrderByTimestampAsc(
            String user1, String user2, String user3, String user4);
    @Query("SELECT DISTINCT CASE " +
            "    WHEN c.senderUsername = :manager THEN c.recipientUsername " +
            "    WHEN c.recipientUsername = :manager THEN c.senderUsername " +
            "END " +
            "FROM ChatMessageEntity c " +
            "WHERE c.senderUsername = :manager OR c.recipientUsername = :manager")
    List<String> findUsersWhoChattedWithManager(@Param("manager") String managerUsername);
}
