package com.example.api_autho.dto;

import com.example.api_autho.model.DishQuantity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MakeOrderDto {
    private Long id;
    private List<DishQuantity> dishes;
    private String specialties;
    private String status;
}
