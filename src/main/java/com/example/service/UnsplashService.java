package com.example.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.example.dto.UnsplashDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnsplashService {
  @Value("${UNSPLASH_API_KEY}")
  private String unSplashAccessKey;

  @Value("${UNSPLASH_API_URL}")
  private String apiUrl;

  private final RestTemplate restTemplate;
  final String endPoint = "https://kr.object.ncloudstorage.com";
  final String regionName = "kr-standard";
  final String objectStorageAccessKey = "R328jTsxPBLHp2cuWF2j";
  final String objectStorageSecretKey = "LrgxfVn4LdAJ4FLYYnCpHeWWgbspSPrb2oz0DwuJ";


  public String getRandomImageJson() {
    String url = apiUrl + "?client_id=" + unSplashAccessKey;
    return restTemplate.getForObject(url, String.class);
  }

  public ByteArrayInputStream getRandomImageStream() throws IOException {
    String url = apiUrl + "?client_id=" + unSplashAccessKey;

    UnsplashDto response = restTemplate.getForObject(url, UnsplashDto.class);

    if (response != null && response.getLinks() != null) {
      String downloadLocation = response.getLinks().getDownloadLocation();
      System.out.println("downloadLocation = " + downloadLocation);
      byte[] imageData = downloadImage(response.getLinks().getDownload());
      if (imageData != null) {
        return new ByteArrayInputStream(imageData);
      }
    }
    return new ByteArrayInputStream(new byte[0]);
  }


// 다른 import 문들과 함께 추가

  public PutObjectResult uploadImage() throws IOException {
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(objectStorageAccessKey, objectStorageSecretKey)))
            .build();

    String bucketName = "unsplash";
    String objectKey = "test/" + UUID.randomUUID().toString() + ".jpg"; // 고유한 파일 이름 생성

    // 다운로드한 이미지 데이터를 바이트 배열로 읽음
    ByteArrayInputStream imageStream = getRandomImageStream();
    byte[] imageData = imageStream.readAllBytes(); // Convert stream to byte array to get the size
    imageStream.close(); // 스트림 닫기

    // 이미지 스트림을 다시 생성
    ByteArrayInputStream uploadStream = new ByteArrayInputStream(imageData);

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(imageData.length); // Set the actual size of the image data
    objectMetadata.setContentType("image/jpeg"); // Set the MIME type of the image

    log.info("File Size = " + imageData.length);

    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, uploadStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead); // 객체를 공개로 설정

    try {
      System.out.format("Uploading file to %s/%s\n", bucketName, objectKey);
      return s3.putObject(putObjectRequest);
    } catch (AmazonS3Exception e) {
      e.printStackTrace();
    } catch (SdkClientException e) {
      e.printStackTrace();
    } finally {
      uploadStream.close(); // 업로드 스트림 닫기
    }

    return null;
  }



  private byte[] downloadImage(String downloadUrl) throws IOException {
    URL url = new URL(downloadUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    try (InputStream in = connection.getInputStream()) {
      return in.readAllBytes();
    }
  }
}
