package org.example.service;

import org.example.dto.UserLoginDTO;
import org.example.dto.UserRegisterDTO;
import org.example.entity.User;
import org.example.enums.UserRole;
import org.example.enums.UserStatus;
import org.example.repository.UserRepository;
import org.example.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    UserRepository userRepository = UserRepository.getInstance();

    private UserService() {

    }

    public static UserService getInstance() {
        return new UserService();
    }

    // User

    public boolean registerUser(UserRegisterDTO userDTO) {
        List<UserRegisterDTO> userList = userRepository.getAllUsers();

        for (UserRegisterDTO data : userList) {
            if (data.phoneNumber().equals(userDTO.phoneNumber()) && data.gmail().equals(userDTO.gmail())) {
                return false;
            }
        }
        Utils.currentUser = new User(userDTO.id(), userDTO.fullName(), userDTO.phoneNumber(), userDTO.gmail(), userDTO.password(), userDTO.userRole(), userDTO.userStatus()); // Set current user
        String userData = "%s#%s#%s#%s#%s#%s#%s\n".formatted(userDTO.id(), userDTO.fullName(), userDTO.phoneNumber(), userDTO.gmail(), userDTO.password(), UserRole.USER, UserStatus.ACTIVE);
        userRepository.addUserToFile(userData, userDTO);

        return true;
    }

    public Optional<User> loginUser(UserLoginDTO userLoginDTO) {
        List<UserRegisterDTO> usersList = userRepository.getAllUsers();

        Optional<User> registeredUser = usersList.stream()
                .filter(userRegisterDTO -> userRegisterDTO.gmail().equals(userLoginDTO.gmail()))
                .filter(userRegisterDTO -> userRegisterDTO.phoneNumber().equals(userLoginDTO.phoneNumber()))
                .filter(userRegisterDTO -> userRegisterDTO.password().equals(userLoginDTO.password()))
                .map(userRegisterDTO -> new User(
                        userRegisterDTO.id(), userRegisterDTO.fullName(),
                        userRegisterDTO.phoneNumber(), userRegisterDTO.gmail(),
                        userRegisterDTO.password(), userRegisterDTO.userRole(),
                        userRegisterDTO.userStatus()))
                .findFirst();

        registeredUser.ifPresent(user -> Utils.currentUser = new User(user.getId(), user.getFullName(), user.getPhoneNumber(),
                user.getGmail(), user.getPassword(), user.getUserRole(), user.getUserStatus()));

        return registeredUser;
    }

    public List<UserRegisterDTO> getActiveUsers() {
        List<UserRegisterDTO> activeUsers = new ArrayList<>();

        for (UserRegisterDTO allUser : userRepository.getAllUsers()) {
            if (allUser.userRole().equals(UserRole.ADMIN)) {
                continue;
            }

            if (allUser.userStatus().equals(UserStatus.ACTIVE)) {
                activeUsers.add(allUser);
            }
        }

        return activeUsers;
    }

    public void blockUserById(String userId) {
        userRepository.changeUserStatusById(userId);
    }

}
