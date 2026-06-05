package com.med.delivery.repository;

import com.med.delivery.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, Boolean isRead);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = ?1 AND n.isRead = false")
    Long countUnreadByUserId(Long userId);
}
