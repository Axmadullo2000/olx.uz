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
import java.util.Optional;

@Data
public class UserRepository {
    private static UserRepository userRepository;

    Path userPath = Path.of("src/main/resources/users.txt");
    Path postPath = Path.of("src/main/resources/posts.txt");

    private UserRepository() {

    }

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }

        return userRepository;
    }

    public Optional<List<UserRegisterDTO>> getAllUsers() {
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

                return Optional.of(list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void addUserToFile(String userData, UserRegisterDTO userDTO) {
        if (!Files.exists(userPath)) {
            try {
                Files.createFile(userPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.writeString(userPath, userData, StandardOpenOption.APPEND);
            Utils.currentUser = new User(userDTO.id(), userDTO.fullName(), userDTO.phoneNumber(), userDTO.gmail(), userDTO.password(), userDTO.userRole(), userDTO.userStatus());

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeUserStatusById(String id) {
        Optional<List<UserRegisterDTO>> allUsers = getAllUsers();

        if (allUsers.isPresent()) {
            List<UserRegisterDTO> userData = allUsers.get();

            for (int i = 0; i < userData.size(); i++) {
                UserRegisterDTO allUser = userData.get(i);
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

                    userData.set(i, updateUser);
                }
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(userPath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            if (allUsers.isPresent()) {
                List<UserRegisterDTO> userData = allUsers.get();

                for (UserRegisterDTO user : userData) {
                    String data = "%s#%s#%s#%s#%s#%s#%s\n".formatted(
                            user.id(),
                            user.fullName(),
                            user.phoneNumber(),
                            user.gmail(),
                            user.password(),
                            user.userRole(),
                            user.userStatus()
                    );

                    writer.write(data);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

