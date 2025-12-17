package com.example.server.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  private final Dotenv dotenv = Dotenv.configure()
    .ignoreIfMissing()
    .load();

  @Bean
  public S3Client s3Client() {
    String accessKey = dotenv.get("AWS_ACCESS_KEY");
    String secretKey = dotenv.get("AWS_SECRET_KEY");
    String region = dotenv.get("AWS_S3_REGION", "us-east-1");

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
      .region(Region.of(region))
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .build();
  }

  @Bean
  public Dotenv dotenv() {
    return dotenv;
  }
}
