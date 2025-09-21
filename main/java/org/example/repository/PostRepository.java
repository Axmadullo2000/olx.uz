package org.example.repository;

import org.example.dto.AddressDTO;
import org.example.dto.PostDTO;
import org.example.enums.HomeType;
import org.example.enums.SellingType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private static PostRepository postRepository;

    Path postPath = Path.of("src/main/resources/posts.txt");

    private PostRepository() {
    }

    public static PostRepository getInstance() {
        if (postRepository == null) {
            postRepository = new PostRepository();
        }

        return postRepository;
    }

    public void createPost(String postData) {
        try {
            if (!Files.exists(postPath)) {
                Files.createFile(postPath);
            } else {
                Files.writeString(postPath, postData, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PostDTO> getAllPosts() {
        List<PostDTO> postDTO = new ArrayList<>();

        try {
            if (!Files.exists(postPath)) {
                Files.createFile(postPath);
            } else {
                List<String> posts = Files.readAllLines(postPath);

                for (String post : posts) {
                    String[] splitPost = post.split("#");

                    postDTO.add(new PostDTO(
                            splitPost[0],
                            HomeType.valueOf(splitPost[1]),
                            new AddressDTO(splitPost[2], splitPost[3], Integer.parseInt(splitPost[4])),
                            Double.parseDouble(splitPost[5]),
                            Integer.parseInt(splitPost[6]),
                            Integer.parseInt(splitPost[7]),
                            SellingType.valueOf(splitPost[8]),
                            splitPost[9]
                    ));
                }
            }

            return postDTO;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFromFile(String postId) {
        List<PostDTO> myPosts = getAllPosts();

        myPosts.removeIf(post -> post.id().equals(postId));

        String postData = "";

        for (PostDTO myPost : myPosts) {
            if (myPost.id().equals(postId)) {
                postData = "%s#%s#%s#%s#%d#%.2f#%d#%d#%s#%s\n".formatted(
                        myPost.id(),
                        myPost.homeType(),
                        myPost.address().city(),
                        myPost.address().street(),
                        myPost.address().homeNumber(),
                        myPost.square(),
                        myPost.roomCount(),
                        myPost.price(),
                        myPost.sellingType(),
                        myPost.currentUserId()
                );
            }
        }

        try {
            Files.writeString(postPath, postData);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
