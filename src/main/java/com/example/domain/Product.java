package com.example.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @Column(name = "product_name")
  public String productName;

  @Column(name = "main_image_url")
  public String mainImageUrl;

  @Column(name = "etag")
  public String eTag;

  @Builder
  public Product(String productName, String mainImageUrl, String eTag) {
    this.productName = productName;
    this.mainImageUrl = mainImageUrl;
    this.eTag = eTag;
  }
}

