package com.zone._blog.media;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.media.dto.MediaContent;
import com.zone._blog.media.dto.MediaDto;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    @Value("${media.upload-directory}")
    private String storageName;
    private Path storageDirectory;

    @PostConstruct
    public void createStorage() {
        this.storageDirectory = Paths.get(storageName).toAbsolutePath().normalize();
        if (!Files.exists(this.storageDirectory)) {
            try {
                Files.createDirectories(this.storageDirectory);
            } catch (IOException e) {
                throw new RuntimeException("Error creating mediaEntity upload directory");
            }
        }
    }

    public MediaServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    @Transactional
    public MediaDto saveMedia(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty, please upload a valid file");
        }

        String storingName = this.sanitizeFilename(file.getOriginalFilename());
        String contentType = this.getMimeType(file.getContentType(), file);
        long size = file.getSize();

        Media mediaEntity = new Media(
                storingName,
                contentType,
                size
        );

        this.uploadFile(file, storingName);
        this.mediaRepository.save(mediaEntity);

        return new MediaDto(
                mediaEntity.getId(),
                mediaEntity.getFilename(),
                mediaEntity.getContentType(),
                mediaEntity.getSize()
        );

    }

    @Override
    public MediaContent getMedia(UUID id) {

        Media mediaEntity = this.mediaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File " + id + " not found"));

        try {
            Path filePath = this.getFullPath(mediaEntity.getFilename());

            Resource resource = new UrlResource(filePath.toUri());

            System.out.println("Resource ===========> " + resource.toString());
            MediaType mediaType = MediaType.parseMediaType(mediaEntity.getContentType());

            if (resource.exists()) {
                return new MediaContent(
                        mediaEntity.getId(),
                        mediaEntity.getFilename(),
                        mediaEntity.getSize(),
                        mediaType,
                        resource
                );
            }

        } catch (IOException e) {
            System.out.println("Upload file exception ===========> " + e.toString());
            throw new RuntimeException("Error getting file");
        }

        throw new ResourceNotFoundException("File not found");

    }

    @Override
    @Transactional
    public MediaDto updateMedia(MultipartFile file, UUID id) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty, please upload a valid file");
        }

        Media mediaEntity = this.mediaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File " + id + "not found"));

        String originalFilename = mediaEntity.getFilename();

        String storingName = this.sanitizeFilename(file.getOriginalFilename());
        String contentType = this.getMimeType(file.getContentType(), file);

        this.uploadFile(file, storingName);
        this.deleteFile(originalFilename);

        mediaEntity.setFilename(storingName);
        mediaEntity.setContentType(contentType);
        mediaEntity.setSize(file.getSize());
        this.mediaRepository.save(mediaEntity);

        return new MediaDto(
                mediaEntity.getId(),
                mediaEntity.getFilename(),
                mediaEntity.getContentType(),
                mediaEntity.getSize()
        );
    }

    @Override
    public void deleteMedia(UUID id) {
        Media mediaEntity = this.mediaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File " + id + "not found"));

        this.deleteFile(mediaEntity.getFilename());
        this.mediaRepository.deleteById(id);
    }

    public String sanitizeFilename(String originalFilename) {
        String cleanPath = StringUtils.cleanPath(Objects.requireNonNull(originalFilename)).trim();
        String extension = "";
        int extentionStart = cleanPath.lastIndexOf(".");

        if (extentionStart > 0) {
            extension += cleanPath.substring(extentionStart);
        }

        return UUID.randomUUID() + extension;
    }

    public void deleteFile(String filename) {
        Path filePath = this.getFullPath(filename);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting mediaEntity");
        }
    }

    public void uploadFile(MultipartFile file, String filename) {
        Path filePath = this.getFullPath(filename);

        // try  {
        //     Files.copy(file.getInputStream(), filePath);
        // } catch (IOException e) {
        //     System.out.println("Upload file exception ===========> " + e.toString());
        //     throw new RuntimeException("Error storing file");
        // }
        try (InputStream inputStream = file.getInputStream(); OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            // byte[] buffer = new byte[8129];
            // int bytesRead;
            // while ((bytesRead = inputStream.read(buffer)) != -1) {
            //     outputStream.write(buffer, 0, bytesRead);
            // }
            // outputStream.close();
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            System.out.println("Upload file exception ===========> " + e.toString());
            throw new RuntimeException("Error storing file");

        }

    }

    public Path getFullPath(String filename) {
        Path filePath = this.storageDirectory.resolve(filename).normalize();

        if (!filePath.startsWith(this.storageDirectory)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        return filePath;

    }

    public String getMimeType(String orignalFileContent, MultipartFile file) {

        Tika tika = new Tika();

        try {
            String mimeType = tika.detect(file.getInputStream());
            if (!mimeType.equals(orignalFileContent)) {
                throw new IllegalArgumentException("File extension and Content mismatch");
            }

            return mimeType;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error getting real content type");

        }

    }

}
