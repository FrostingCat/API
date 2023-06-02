package com.example.api_autho.dto;

import com.example.api_autho.model.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;

    public Order toUser(){
        Order order = new Order();
        order.setId(id);

        return order;
    }

    public static UserDto fromUser(Order order) {
        UserDto userDto = new UserDto();
        userDto.setId(order.getId());

        return userDto;
    }
}
