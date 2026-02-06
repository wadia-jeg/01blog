package com.zone._blog.likes;

import com.zone._blog.likes.dto.LikeResponse;

public class LikeMapper {

    public static LikeResponse toLikeReponse(Like likeEntity) {
        return new LikeResponse(
                likeEntity.getId(),
                likeEntity.getPost().getId(),
                likeEntity.getUser().getId(),
                likeEntity.getCreatedAt()
        );
    }

}
