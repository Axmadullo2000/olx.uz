package org.example.controller;

import org.example.dto.UserLoginDTO;
import org.example.dto.UserRegisterDTO;
import org.example.entity.User;
import org.example.enums.UserRole;
import org.example.enums.UserStatus;
import org.example.service.UserService;
import org.example.utils.Mailing;
import org.example.utils.Utils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class MainController {
    private static MainController mainController;

    private final UserService userService = UserService.getInstance();
    private final PostController postController = PostController.getInstance();

    private MainController() {
    }

    public static MainController getInstance() {
        if (mainController == null) {
            mainController = new MainController();
        }

        return mainController;
    }

    public void start() {
        while (true) {
            System.out.println("""
                    1. Sign In
                    2. Sign Up
                    0. Exit
                    """);

            int option = Utils.getNumber("Choose an option");

            switch (option) {
                case 1 -> signIn();
                case 2 -> signUp();
                case 0 -> {
                    System.out.println("Good bye! We'll be happy to see you next time!");
                    return;
                }
            }
        }
    }

    private void signUp() {
        String fullName = Utils.getText("Enter your full name");
        String phoneNumber = Utils.getText("Enter your phone number");
        String gmail = Utils.getText("Enter your gmail");
        String password = Utils.getText("Enter your password");

        int code = new Random().nextInt(100000, 1000000);

        try {
            Mailing.sendMessage(fullName, gmail, code);
            LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);

            int verificationCode = Utils.getNumber("Enter verification code");

            if (LocalDateTime.now().isBefore(localDateTime)) {
                if (verificationCode == code) {
                    UserRegisterDTO userRegisterDTO = new UserRegisterDTO(UUID.randomUUID().toString(), fullName, phoneNumber, gmail, password, UserRole.USER, UserStatus.ACTIVE);

                    boolean response = userService.registerUser(userRegisterDTO);

                    if (response) {
                        userMenu();
                    } else {
                        System.out.println("You are already registered on this platform! Sign in to use the program!");
                    }
                } else {
                    System.out.println("Code is not valid");
                }
            } else {
                System.out.println("Session time is over");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void signIn() {
        String gmail = Utils.getText("Enter your gmail");
        String phoneNumber = Utils.getText("Enter your phone number");
        String password = Utils.getText("Enter your password");

        UserLoginDTO userLoginDTO = new UserLoginDTO(gmail, phoneNumber, password);

        Optional<User> user = userService.loginUser(userLoginDTO);

        if (user.isPresent()) {
            Utils.currentUser = user.get(); // Set current user
            if (user.get().getUserRole().equals(UserRole.USER)) {
                userMenu();
            } else if (user.get().getUserRole().equals(UserRole.ADMIN)) {
                adminMenu();
            }
        } else {
            System.out.println("Data is not valid! That's why user is not found!");
        }
    }

    private void userMenu() {
        System.out.println("Hello " + Utils.currentUser.getFullName());

        while (true) {
            System.out.println("""
                    1. Add new post
                    2. View my posts
                    3. Delete my posts
                    4. Search through global post
                    0. Exit
                    """);

            int option = Utils.getNumber("Choose an option");

            switch (option) {
                case 1 -> postController.addNewPost();
                case 2 -> postController.viewMyPosts();
                case 3 -> postController.deleteMyPost();
                case 4 -> postController.searchThroughPosts();
                case 0 -> {
                    System.out.println("We'll be happy to see you next time!");
                    return;
                }
            }
        }
    }

    private void adminMenu() {
        System.out.println("Hi Admin!");

        while (true) {
            System.out.println("""
                    1. All posts
                    2. Block User
                    0. Exit
                    """);

            int option = Utils.getNumber("Choose an option");

            switch (option) {
                case 1 -> postController.showAllPosts();
                case 2 -> blockUser();
                case 0 -> {
                    System.out.println("Good bye!");
                    return;
                }
            }
        }
    }

    private void blockUser() {
        System.out.println("You will be able to block users");

        List<UserRegisterDTO> activeUsers = userService.getActiveUsers();

        for (UserRegisterDTO activeUser : activeUsers) {
            System.out.println(activeUser.id());
            System.out.println("#" + activeUser.fullName());
        }

        String userId = Utils.getText("Enter user id");

        userService.blockUserById(userId);
    }
}
