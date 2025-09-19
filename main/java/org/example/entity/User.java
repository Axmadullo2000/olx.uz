package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.enums.UserRole;
import org.example.enums.UserStatus;

@Data
@AllArgsConstructor
public class User {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String gmail;
    private String password;
    private UserRole userRole;
    private UserStatus userStatus;

    @Override
    public String toString() {
        return "User{id=%s, full name=%s, phone number=%s, gmail=%s, password=%s, userStatus=%s}".formatted(id, fullName, phoneNumber, gmail, password, userStatus);
    }
}
