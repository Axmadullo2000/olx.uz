package org.example.service;

import org.example.dto.PostDTO;
import org.example.dto.UserRegisterDTO;
import org.example.enums.HomeType;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.utils.Utils;

import java.util.List;
import java.util.Optional;

public class PostService {
    private static PostService postService;

    PostRepository postRepository = PostRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();


    public PostService() {
    }

    public static PostService getInstance() {
        if (postService == null) {
            postService = new PostService();
        }

        return postService;
    }

    public boolean createPost(PostDTO postDTO) {
        if (postDTO.homeType().name().length() < 4
                || postDTO.sellingType().name().length() < 4) return false;

        List<PostDTO> myPosts = postRepository.getAllPosts();

        long count = myPosts.stream()
                .filter(postDTO1 -> postDTO1.homeType().equals(postDTO.homeType()))
                .filter(postDTO1 -> postDTO1.id().equals(postDTO.id()))
                .count();

        if (count > 0) return false;

        String postData = "%s#%s#%s#%s#%d#%.2f#%d#%d#%s#%s\n".formatted(
                postDTO.id(),
                postDTO.homeType(),
                postDTO.address().city(),
                postDTO.address().street(),
                postDTO.address().homeNumber(),
                postDTO.square(),
                postDTO.roomCount(),
                postDTO.price(),
                postDTO.sellingType(),
                postDTO.currentUserId()
        );

        postRepository.createPost(postData);

        return true;
    }

    public List<PostDTO> getMyPosts() {
        return postRepository.getAllPosts().stream()
                .filter(postDTO -> postDTO.currentUserId().equals(Utils.currentUser.getId()))
                .toList();
    }

    public void deletePost(String postId) {
        postRepository.deleteFromFile(postId);
    }

    public List<PostDTO> getGlobePosts(String city, int minPrice, int maxPrice, String homeType, int roomCount) {
        return postRepository.getAllPosts().stream()
                .filter(postDTO -> postDTO.address().city().equalsIgnoreCase(city))
                .filter(postDTO -> postDTO.price() > minPrice && postDTO.price() < maxPrice)
                .filter(postDTO -> postDTO.homeType().equals(HomeType.valueOf(homeType)))
                .filter(postDTO -> postDTO.roomCount() == roomCount)
                .toList();
    }

    public String getFullNameByPostId(String postId) {
        List<PostDTO> list = postRepository.getAllPosts().stream()
                .filter(postDTO -> postDTO.id().equals(postId))
                .toList();

        for (PostDTO post : list) {
            if (post.id().equals(postId)) {
                Optional<List<UserRegisterDTO>> data = userRepository.getAllUsers();

                if (data.isPresent()) {
                    List<UserRegisterDTO> userRegisterDTOS = data.get();

                    return userRegisterDTOS.stream()
                            .filter(userRegisterDTO -> userRegisterDTO.id().equals(post.currentUserId()))
                            .map(UserRegisterDTO::fullName)
                            .toList().get(0);
                }
            }
        }

        return null;
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.getAllPosts();
    }
}
