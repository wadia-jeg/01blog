package com.zone._blog.media;

import com.zone._blog.media.dto.MediaDto;

public class MediaMapper {

    public static Media toMedia(MediaDto mediaDto) {

        return new Media(
                mediaDto.getFilename(),
                mediaDto.getContentType(),
                mediaDto.getSize()
        );
    }

    public static MediaDto toMediaDto(Media mediaEntity) {

        return new MediaDto(
                mediaEntity.getId(),
                mediaEntity.getFilename(),
                mediaEntity.getContentType(),
                mediaEntity.getSize()
        );
    }

}
