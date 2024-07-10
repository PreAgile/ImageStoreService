package com.example.service;

import com.example.dto.UnsplashDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class UnsplashService {
  @Value("${UNSPLASH_API_KEY}")
  private String accessKey;

  @Value("${UNSPLASH_API_URL}")
  private String apiUrl;

  private final RestTemplate restTemplate;


  public String getRandomImageJson() {
    String url = apiUrl + "?client_id=" + accessKey;
    return restTemplate.getForObject(url, String.class);
  }

  public byte[] getRandomImage() throws IOException {
    String url = apiUrl + "?client_id=" + accessKey;

    UnsplashDto response = restTemplate.getForObject(url, UnsplashDto.class);

    if (response != null && response.getLinks() != null) {
      String downloadLocation = response.getLinks().getDownloadLocation();
      System.out.println("downloadLocation = " + downloadLocation);
      return downloadImage(response.getLinks().getDownload());
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
