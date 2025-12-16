package com.example.server.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Value("${aws.s3.region:us-east-1}")
  private String region;

  @Value("${aws.access-key:}")
  private String accessKey;

  @Value("${aws.secret-key:}")
  private String secretKey;

  @Bean
  @ConditionalOnProperty(name = "aws.access-key", matchIfMissing = false)
  public S3Client s3Client() {
    if (accessKey == null || accessKey.isEmpty() || secretKey == null || secretKey.isEmpty()) {
      return null;
    }

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }
}
