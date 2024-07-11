package com.example.dto;

import com.example.domain.Product;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

  private String productName;

  @NotNull
  private String mainImageUrl;

  public ProductDto(Product product) {
    this.productName = product.getProductName();
    this.mainImageUrl = product.getMainImageUrl();
  }
}
