package com.utar.uhauction.controller;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.yaml.snakeyaml.Yaml;
import java.util.Map;
@Service
public class MinioUploadController {

    private static MinioClient minioClient;
    private static String bucketName;




    public static void init() {
        try {
            // Load YAML configuration file
            InputStream inputStream = MinioUploadController.class.getClassLoader().getResourceAsStream("application-dev.yaml");
            if (inputStream != null) {
                Yaml yaml = new Yaml();
                Map<String, Object> yamlData = yaml.load(inputStream);

                // Extract the minio map from YAML data
                Map<String, String> minioProperties = (Map<String, String>) yamlData.get("minio");

                // Extract properties from the minio map
                String minioEndpoint = minioProperties.get("endpoint");
                String minioAccessKey = minioProperties.get("access-key");
                String minioSecretKey = minioProperties.get("secret-key");
                String minioBucketName = minioProperties.get("bucket-name");
                bucketName = minioBucketName;

                // Initialize MinioClient
                minioClient = MinioClient.builder()
                        .endpoint(minioEndpoint)
                        .credentials(minioAccessKey, minioSecretKey)
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static String uploadImgFile(MultipartFile file, String fileName) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/item/img/"+fileName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
