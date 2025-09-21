package org.example.controller;

import org.example.dto.AddressDTO;
import org.example.dto.PostDTO;
import org.example.enums.HomeType;
import org.example.enums.SellingType;
import org.example.enums.UserStatus;
import org.example.service.PostService;
import org.example.utils.Utils;

import java.util.List;
import java.util.UUID;

public class PostController {
    private static PostController postController;

    private final PostService postService = PostService.getInstance();

    private PostController() {
    }

    public static PostController getInstance() {
        if (postController == null) {
            postController = new PostController();
        }

        return postController;
    }

    public void addNewPost() {
        if (Utils.currentUser.getUserStatus().equals(UserStatus.BLOCK)) {
            System.out.println("You are blocked by admins");
            return;
        }

        System.out.println("Fill up necessary fields below about the home!");

        try {
            String homeType = Utils.getText("Choose home type: (APARTMENT, HOME, HOUSE)");
            String city = Utils.getText("Enter city");
            String street = Utils.getText("Enter street");
            int homeNumber = Utils.getNumber("Enter home number");
            double square = Utils.getFloat("Enter square of home");
            int roomCount = Utils.getNumber("Enter room count");
            String sellingType = Utils.getText("Choose selling type: (SELL, RENT)");
            int price = Utils.getNumber("Enter price");

            PostDTO postDTO = new PostDTO(
                    UUID.randomUUID().toString(),
                    HomeType.valueOf(homeType),
                    new AddressDTO(city, street, homeNumber),
                    square,
                    roomCount,
                    price,
                    SellingType.valueOf(sellingType),
                    Utils.currentUser.getId()
            );

            boolean responseFromPost = postService.createPost(postDTO);

            if (responseFromPost) {
                System.out.println("Post is successfully created!");
            } else {
                System.out.println("Error occurred while creating post");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error occurred while adding data");
        }
    }

    public void viewMyPosts() {
        System.out.println("My Posts");

        List<PostDTO> posts = postService.getMyPosts();

        if (posts.isEmpty()) {
            System.out.println("No posts found!");
            return;
        }

        postShowInterface(posts);
    }

    public void deleteMyPost() {
        System.out.println("Which post are you going to delete?");

        List<PostDTO> myPosts = postService.getMyPosts();

        for (PostDTO myPost : myPosts) {
            System.out.println(myPost);
        }

        String text = Utils.getText("Enter post id");

        postService.deletePost(text);
    }

    public void searchThroughPosts() {
        System.out.println("I believe that you can find your dream home here: ");
        String city = Utils.getText("Enter city");
        int minPrice = Utils.getNumber("Enter min price in dollars");
        int maxPrice = Utils.getNumber("Enter max price in dollars");
        String homeType = Utils.getText("Choose home type (APARTMENT, HOME, HOUSE)");
        int roomCount = Utils.getNumber("Enter room count");

        List<PostDTO> foundPosts = postService.getGlobePosts(city, minPrice, maxPrice, homeType, roomCount);

        if (foundPosts.isEmpty()) {
            System.out.println("Nothing to show!");
            return;
        }

        postShowInterface(foundPosts);
    }

    public void showAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        postShowInterface(posts);
    }

    private void postShowInterface(List<PostDTO> posts) {
        for (PostDTO post : posts) {
            System.out.println("---------------------------------------------");
            System.out.println(" " + post.address().city() + " " + post.address().street());
            System.out.println(" " + post.roomCount() + " rooms");
            System.out.println(" $ " + post.price());
            System.out.println(" " + post.square() + " m'2");
            System.out.println(" #" + post.homeType().toString());
            System.out.println(" #" + post.sellingType().toString());
            System.out.println(" #" + postService.getFullNameByPostId(post.id()).trim());
            System.out.println("---------------------------------------------");
        }
    }
}
