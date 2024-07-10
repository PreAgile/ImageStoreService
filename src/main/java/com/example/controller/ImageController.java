package com.example.controller;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.service.UnsplashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/images")
public class ImageController {

  private final UnsplashService unsplashService;
  @GetMapping("/unsplash")
  public String getUnsplashJson() throws IOException {
    return unsplashService.getRandomImageJson();
  }

  @PostMapping("/unsplash")
  public PutObjectResult postImage() throws IOException {
     return unsplashService.uploadImage();
  }
}
