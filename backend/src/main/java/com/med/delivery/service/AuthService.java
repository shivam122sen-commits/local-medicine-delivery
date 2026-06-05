package com.med.delivery.service;

import com.med.delivery.dto.AuthResponse;
import com.med.delivery.dto.LoginRequest;
import com.med.delivery.dto.RegisterRequest;
import com.med.delivery.model.User;
import com.med.delivery.repository.UserRepository;
import com.med.delivery.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return new AuthResponse(null, null, "Email already exists!");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobile(request.getMobile());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);

        // Set role-specific fields
        if (request.getRole() == User.Role.CHEMIST) {
            user.setPharmacyName(request.getPharmacyName());
            user.setLicenseNumber(request.getLicenseNumber());
            user.setRating(5.0);
        } else if (request.getRole() == User.Role.DELIVERY) {
            user.setVehicleType(request.getVehicleType());
            user.setVehicleNumber(request.getVehicleNumber());
            user.setRating(5.0);
            user.setTotalDeliveries(0);
            user.setIsOnline(false);
        }

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId(), savedUser.getRole().toString());

        return new AuthResponse(token, savedUser, "Registration successful!");
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        
        if (userOptional.isEmpty()) {
            return new AuthResponse(null, null, "Invalid email or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(null, null, "Invalid email or password");
        }

        if (!user.getIsActive()) {
            return new AuthResponse(null, null, "Account is deactivated");
        }

        // Handle remember me
        if (request.getRememberMe() != null && request.getRememberMe()) {
            String rememberToken = UUID.randomUUID().toString();
            user.setRememberMeToken(rememberToken);
            userRepository.save(user);
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().toString());

        return new AuthResponse(token, user, "Login successful!");
    }
}
