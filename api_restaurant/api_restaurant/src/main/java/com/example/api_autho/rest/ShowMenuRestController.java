package com.example.api_autho.rest;

import com.example.api_autho.model.Dish;
import com.example.api_autho.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/menu/")
public class ShowMenuRestController {
    private final OrderService orderService;

    @GetMapping("show")
    public ResponseEntity makeOrder() {
        List<Dish> dishes = orderService.findDishesMenu();

        Map<Object, Object> response = new HashMap<>();
        response.put("dishes", dishes);

        return ResponseEntity.ok(response);
    }
}
