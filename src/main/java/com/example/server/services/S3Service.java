package com.example.server.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

  @Autowired(required = false)
  private S3Client s3Client;

  @Autowired
  private Dotenv dotenv;

  private String getBucketName() {
    return dotenv.get("AWS_S3_BUCKET_NAME");
  }

  private String getRegion() {
    return dotenv.get("AWS_S3_REGION");
  }

  private boolean isConfigured() {
    String bucketName = getBucketName();
    return s3Client != null && bucketName != null && !bucketName.isEmpty();
  }

  /**
   * @param file arquivo a ser enviado
   * @param folder pasta dentro do bucket (ex: "avatars", "posts")
   * @return url do file no s3
   */
  public String uploadFile(MultipartFile file, String folder) {
    if (!isConfigured()) {
      throw new RuntimeException("deu erro de ler as keys da aws denovoooo");
    }

    try {
      String originalFilename = file.getOriginalFilename();
      String extension = originalFilename != null && originalFilename.contains(".")
          ? originalFilename.substring(originalFilename.lastIndexOf("."))
          : "";
      
      String key = folder + "/" + UUID.randomUUID().toString() + extension;

      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(getBucketName())
          .key(key)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

      return getFileUrl(key);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao fazer upload do arquivo", e);
    }
  }

  /**
   * deleta do s3
   * @param fileUrl 
   */
  public void deleteFile(String fileUrl) {
    if (!isConfigured()) return;

    String key = extractKeyFromUrl(fileUrl);
    
    if (key == null) return;

    DeleteObjectRequest request = DeleteObjectRequest.builder()
        .bucket(getBucketName())
        .key(key)
        .build();

    s3Client.deleteObject(request);
  }

  private String getFileUrl(String key) {
    return String.format("https://%s.s3.%s.amazonaws.com/%s", getBucketName(), getRegion(), key);
  }

  private String extractKeyFromUrl(String fileUrl) {
    if (fileUrl == null || !fileUrl.contains(".amazonaws.com/")) {
      return null;
    }
    return fileUrl.substring(fileUrl.indexOf(".amazonaws.com/") + 15);
  }
}
