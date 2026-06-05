package com.med.delivery.controller;

import com.med.delivery.model.Address;
import com.med.delivery.model.User;
import com.med.delivery.repository.AddressRepository;
import com.med.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setName(updatedUser.getName());
        user.setMobile(updatedUser.getMobile());
        user.setAddress(updatedUser.getAddress());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setUpdatedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwords) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String currentPassword = passwords.get("currentPassword");
        String newPassword = passwords.get("newPassword");
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/{userId}/two-factor")
    public ResponseEntity<User> toggleTwoFactor(@PathVariable Long userId, @RequestParam Boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setTwoFactorEnabled(enabled);
        user.setUpdatedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/delivery/online")
    public ResponseEntity<List<User>> getOnlineDeliveryPartners() {
        return ResponseEntity.ok(userRepository.findByRoleAndIsOnline(User.Role.DELIVERY, true));
    }

    @PutMapping("/{userId}/online-status")
    public ResponseEntity<User> updateOnlineStatus(@PathVariable Long userId, @RequestParam Boolean isOnline) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setIsOnline(isOnline);
        user.setUpdatedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Address management
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressRepository.findByUserId(userId));
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<Address> addAddress(@PathVariable Long userId, @RequestBody Address address) {
        address.setUserId(userId);
        address.setCreatedAt(LocalDateTime.now());
        
        // If this is set as default, unset other defaults
        if (address.getIsDefault()) {
            List<Address> existingAddresses = addressRepository.findByUserId(userId);
            existingAddresses.forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }
        
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressRepository.deleteById(addressId);
        return ResponseEntity.ok().build();
    }
}
