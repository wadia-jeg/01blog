package com.zone._blog.media.dto;

import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record MediaContent(
        UUID id,
        String filename,
        long size,
        MediaType mediaType,
        Resource resource
        ) {

    public UUID getId() {
        return id;
    }

    // public void setId(UUID id) {
    //     id = id;
    // }
    public String getFilename() {
        return filename;
    }

    // public void setFilename(String filename) {
    //     filename = filename;
    // }
    public MediaType getMediaType() {
        return mediaType;
    }

    // public void setMediaType(MediaType mediaType) {
    //     mediaType = mediaType;
    // }
    public Resource getResource() {
        return resource;
    }

    // public void setResource(Resource resource) {
    //     resource = resource;
    // }
    public long getSize() {
        return size;
    }

}
