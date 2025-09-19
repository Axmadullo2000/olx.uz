package org.example.repository;

import lombok.Data;

import org.example.dto.UserRegisterDTO;
import org.example.entity.User;
import org.example.enums.UserRole;
import org.example.enums.UserStatus;
import org.example.utils.Utils;

import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRepository {
    Path userPath = Path.of("src/main/resources/users.txt");
    Path postPath = Path.of("src/main/resources/posts.txt");

    private UserRepository() {

    }

    public static UserRepository getInstance() {
        return new UserRepository();
    }

    public List<UserRegisterDTO> getAllUsers() {
        List<UserRegisterDTO> list = new ArrayList<>();

        try {
            if (!Files.exists(userPath)) {
                Files.createFile(userPath);
            } else {
                List<String> userList = Files.readAllLines(userPath);

                for (String userLine : userList) {
                    String[] data = userLine.split("#");

                    list.add(new UserRegisterDTO(data[0], data[1], data[2], data[3], data[4], UserRole.valueOf(data[5]), UserStatus.valueOf(data[6])));
                }

                return list;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addUserToFile(String userData, UserRegisterDTO userDTO) {
        try {
            Files.writeString(userPath, userData, StandardOpenOption.APPEND);
            Utils.currentUser = new User(userDTO.id(), userDTO.fullName(), userDTO.phoneNumber(), userDTO.gmail(), userDTO.password(), userDTO.userRole(), userDTO.userStatus());

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeUserStatusById(String id) {
        List<UserRegisterDTO> allUsers = getAllUsers();

        for (int i = 0; i < allUsers.size(); i++) {
            UserRegisterDTO allUser = allUsers.get(i);
            if (allUser.id().equals(id)) {

                UserRegisterDTO updateUser = new UserRegisterDTO(
                        allUser.id(),
                        allUser.fullName(),
                        allUser.phoneNumber(),
                        allUser.gmail(),
                        allUser.password(),
                        allUser.userRole(),
                        UserStatus.BLOCK
                );

                allUsers.set(i, updateUser);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(userPath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (UserRegisterDTO user : allUsers) {
                String userData = "%s#%s#%s#%s#%s#%s#%s\n".formatted(
                        user.id(),
                        user.fullName(),
                        user.phoneNumber(),
                        user.gmail(),
                        user.password(),
                        user.userRole(),
                        user.userStatus()
                );

                writer.write(userData);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}

