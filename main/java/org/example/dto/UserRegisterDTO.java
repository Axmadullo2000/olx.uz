package org.example.dto;

import org.example.enums.UserRole;
import org.example.enums.UserStatus;

public record UserRegisterDTO(String id, String fullName, String phoneNumber, String gmail, String password, UserRole userRole, UserStatus userStatus) {}
