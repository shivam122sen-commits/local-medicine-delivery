package com.med.delivery.controller;

import com.med.delivery.model.Wishlist;
import com.med.delivery.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getUserWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistRepository.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getWishlistCount(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistRepository.countByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Wishlist> addToWishlist(@RequestBody Wishlist wishlist) {
        // Check if already exists
        if (wishlistRepository.findByUserIdAndInventoryId(
                wishlist.getUserId(), wishlist.getInventoryId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
        wishlist.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(wishlistRepository.save(wishlist));
    }

    @DeleteMapping("/user/{userId}/item/{inventoryId}")
    @Transactional
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long userId, @PathVariable Long inventoryId) {
        wishlistRepository.deleteByUserIdAndInventoryId(userId, inventoryId);
        return ResponseEntity.ok().build();
    }
}
