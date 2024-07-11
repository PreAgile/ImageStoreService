package com.example.controller;

import com.example.domain.Product;
import com.example.dto.ProductDto;
import com.example.service.UnsplashService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/images")
public class ImageController {

  private final UnsplashService unsplashService;
  @GetMapping("/unsplash")
  public String getUnsplashJson() {
    return unsplashService.getRandomImageJson();
  }

  @PostMapping("/unsplash")
  public ResponseEntity<ProductDto> postImage() throws IOException {
    ProductDto productDto = unsplashService.uploadImage();
    return ResponseEntity.ok(productDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getImage(@PathVariable Integer id, @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
    Product product = unsplashService.getProductById(BigInteger.valueOf(id));
    String etag = product.getETag();

    if (etag.equals(ifNoneMatch)) {
      return ResponseEntity.status(304).eTag(etag).build();
    } else {
      return ResponseEntity.ok().eTag(etag).body(product.getMainImageUrl());
    }
  }
}
