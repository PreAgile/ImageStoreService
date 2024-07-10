package com.example.controller;

import com.example.service.UnsplashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
