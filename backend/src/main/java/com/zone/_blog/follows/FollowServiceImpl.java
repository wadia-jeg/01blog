package com.zone._blog.follows;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.follows.dto.FollowRequest;
import com.zone._blog.follows.dto.FollowResponse;
import com.zone._blog.users.UserInfo;
import com.zone._blog.users.UserRepository;
import com.zone._blog.users.UserService;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FollowServiceImpl(
            FollowRepository followRepository,
            UserRepository userRepository,
            UserService userService
    ) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public boolean isUserFollowed(UUID followerId) {
        UserInfo currentUser = this.userService.getCurrentUser();
        return this.followRepository.existsByFollowingIdAndFollowerId(currentUser.getId(), followerId);

    }

    @Override
    public boolean isUserFollowing(UUID followingId) {
        UserInfo currentUser = this.userService.getCurrentUser();
        return this.followRepository.existsByFollowerIdAndFollowingId(currentUser.getId(), followingId);

    }

    @Override
    public List<FollowResponse> getFollowers() {
        UserInfo currentUser = this.userService.getCurrentUser();
        return this.followRepository.findAllByFollowingId(currentUser.getId());

    }

    @Override
    public List<FollowResponse> getFollowers(UUID followingId) {
        return this.followRepository.findAllByFollowingId(followingId);

    }

    @Override
    public List<FollowResponse> getFollowings() {
        UserInfo currentUser = this.userService.getCurrentUser();
        return this.followRepository.findAllByFollowerId(currentUser.getId());

    }

    @Override
    public List<FollowResponse> getFollowings(UUID followerId) {
        return this.followRepository.findAllByFollowerId(followerId);

    }

    @Override
    public FollowResponse toggleFollow(FollowRequest followRequest) {

        UserInfo currentUser = this.userService.getCurrentUser();
        UUID followingUserId = followRequest.followingId();

        if (followingUserId.equals(currentUser.getId())) {
            throw new IllegalArgumentException("Invalid following user");
        }

        Optional<Follow> followOption = this.followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), followingUserId);

        if (followOption.isPresent()) {
            Follow follow = followOption.get();
            this.followRepository.deleteById(follow.getId());
            return null;
        }

        UserInfo followingUser = this.userRepository.findById(followingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Following user not found"));

        if (followingUser.isAccountNonLocked()) {
            throw new IllegalArgumentException("Can not follow this user");
        }

        Follow followEntity = new Follow(currentUser, followingUser);

        this.followRepository.save(followEntity);

        return FollowMapper.toFollowResponse(followEntity);
    }

    public FollowResponse follow(FollowRequest followRequest) {
        UserInfo currentUser = this.userService.getCurrentUser();
        UUID followingUserId = followRequest.followingId();

        if (followingUserId.equals(currentUser.getId())) {
            throw new IllegalArgumentException("Invalid following user");
        }

        Optional<Follow> followOption = this.followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), followingUserId);

        if (followOption.isPresent()) {
            throw new IllegalArgumentException("You already follow this user");
        }

        UserInfo followingUser = this.userRepository.findById(followingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Following user not found"));

        if (followingUser.isAccountNonLocked()) {
            throw new IllegalArgumentException("Can not follow this user");
        }

        Follow followEntity = new Follow(currentUser, followingUser);

        this.followRepository.save(followEntity);

        return FollowMapper.toFollowResponse(followEntity);
    }

    @Override
    public void unfollow(UUID followingId) {
        UserInfo currentUser = this.userService.getCurrentUser();

        Follow follow = this.followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), followingId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow not found"));

        this.followRepository.deleteById(follow.getId());
    }
}
