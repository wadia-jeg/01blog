package com.zone._blog.media;

import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.media.dto.MediaContent;
import com.zone._blog.media.dto.MediaDto;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("")
    public ResponseEntity<MediaDto> uploadMedia(@RequestParam("file") MultipartFile mediaRequest) {
        System.out.println(mediaRequest.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mediaService.saveMedia(mediaRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getMedia(@PathVariable UUID id) {
        MediaContent mediaDto = this.mediaService.getMedia(id);

        System.out.println("Media Type ===========> " + mediaDto.getMediaType());

        return ResponseEntity.status(HttpStatus.OK).contentType(mediaDto.getMediaType()).body(mediaDto.getResource());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaDto> updateMedia(@RequestParam("file") MultipartFile mediaRequest, @PathVariable UUID id) {
        return ResponseEntity.ok(this.mediaService.updateMedia(mediaRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedia(@PathVariable UUID id) {
        this.mediaService.deleteMedia(id);
        return ResponseEntity.ok("File deleted succefully");
    }

}
