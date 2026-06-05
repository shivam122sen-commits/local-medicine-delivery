package com.med.delivery.repository;

import com.med.delivery.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndInventoryId(Long userId, Long inventoryId);
    
    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.userId = ?1")
    Long countByUserId(Long userId);
    
    void deleteByUserIdAndInventoryId(Long userId, Long inventoryId);
}
