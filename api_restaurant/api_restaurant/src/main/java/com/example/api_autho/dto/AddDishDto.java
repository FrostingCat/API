package com.example.api_autho.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddDishDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int quantity;
}
