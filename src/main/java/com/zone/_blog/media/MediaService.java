package com.zone._blog.media;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.media.dto.MediaDto;
import com.zone._blog.media.dto.MediaContent;

public interface MediaService {

    public MediaDto saveMedia(MultipartFile file);

    public MediaContent getMedia(UUID id);

    public MediaDto updateMedia(MultipartFile file, UUID id);

    public void deleteMedia(UUID id);

}
