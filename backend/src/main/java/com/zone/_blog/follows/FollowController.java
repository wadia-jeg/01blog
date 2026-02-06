package com.zone._blog.follows;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.follows.dto.FollowRequest;
import com.zone._blog.follows.dto.FollowResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${app.api.v1}/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")

    public ResponseEntity<FollowResponse> toggleFollow(@Valid @RequestBody FollowRequest followRequest) {
        FollowResponse followResponse = this.followService.toggleFollow(followRequest);

        if (followResponse == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(followResponse);
    }

    // @PostMapping("/follow")
    // public ResponseEntity<FollowResponse> follow(@Valid @RequestBody FollowRequest followRequest) {
    //     FollowResponse followResponse = this.followService.createFollow(followRequest);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(followResponse);
    // }
    @GetMapping("/me/followers")
    public ResponseEntity<List<FollowResponse>> getCurrentUserFollowers() {
        return ResponseEntity.ok(this.followService.getFollowers());
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable UUID userId) {
        return ResponseEntity.ok(this.followService.getFollowers(userId));
    }

    @GetMapping("/me/followings")
    public ResponseEntity<List<FollowResponse>> getCurrentUserFollowings() {
        return ResponseEntity.ok(this.followService.getFollowings());
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<FollowResponse>> getFollowings(@PathVariable UUID userId) {
        return ResponseEntity.ok(this.followService.getFollowings(userId));
    }

    @DeleteMapping("/unfollow/{id}")
    public ResponseEntity<String> unfollow(@PathVariable UUID id) {
        this.followService.unfollow(id);
        return ResponseEntity.ok("Follow deleted succefully");
    }

}
