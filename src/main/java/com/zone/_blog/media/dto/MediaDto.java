package com.zone._blog.media.dto;

import java.util.UUID;

public record MediaDto(
        UUID id,
        String filename,
        String contentType,
        long size
        ) {

    public UUID getId() {
        return this.id;
    }

    // public void setId(UUID id) {
    //     this.id = id;
    // }
    public String getFilename() {
        return this.filename;
    }

    // public void setFilename(String filename) {
    //     this.filename = filename;
    // }
    public String getContentType() {
        return this.contentType;
    }

    // public void setContentType(String contentType) {
    //     this.contentType = contentType;
    // }
    public long getSize() {
        return this.size;
    }

    // public void setSize(long size) {
    //     this.size = size;
    // }
}
