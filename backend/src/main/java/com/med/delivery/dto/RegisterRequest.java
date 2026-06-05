package com.med.delivery.dto;

import com.med.delivery.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String address;
    private User.Role role;
    
    // For chemists
    private String pharmacyName;
    private String licenseNumber;
    
    // For delivery partners
    private String vehicleType;
    private String vehicleNumber;
}
