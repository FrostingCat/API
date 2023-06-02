package com.example.api_autho.rest;

import com.example.api_autho.dto.AddDishDto;
import com.example.api_autho.dto.ChangeDishDto;
import com.example.api_autho.dto.DeleteDishDto;
import com.example.api_autho.model.Dish;
import com.example.api_autho.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/dish/")
public class ChangeDishRestController {
    private final OrderService orderService;

    @PostMapping("add")
    public ResponseEntity addDish(@RequestBody AddDishDto addDishDto) {
        if (orderService.checkManager(addDishDto.getId())) {
            return new ResponseEntity<>("User is not a manager", HttpStatus.BAD_REQUEST);
        }

        Dish dish = new Dish();
        dish.setName(addDishDto.getName());
        dish.setDescription(addDishDto.getDescription());
        dish.setPrice(addDishDto.getPrice());
        dish.setQuantity(addDishDto.getQuantity());

        orderService.addNewDish(dish);

        return ResponseEntity.ok("Dish is added");
    }

    @PostMapping("delete")
    public ResponseEntity deleteDish(@RequestBody DeleteDishDto deleteDishDto) {
        if (orderService.checkManager(deleteDishDto.getId())) {
            return new ResponseEntity<>("User is not a manager", HttpStatus.BAD_REQUEST);
        }

        orderService.deleteDish(deleteDishDto.getName());

        return ResponseEntity.ok("Dish is deleted");
    }

    @PostMapping("change")
    public ResponseEntity changeDish(@RequestBody ChangeDishDto changeDishDto) {
        if (orderService.checkManager(changeDishDto.getId())) {
            return new ResponseEntity<>("User is not a manager", HttpStatus.BAD_REQUEST);
        }

        Dish dish = new Dish();
        dish.setName(changeDishDto.getName());
        dish.setDescription(changeDishDto.getDescription());
        dish.setPrice(changeDishDto.getPrice());
        dish.setQuantity(changeDishDto.getQuantity());

        orderService.changeDish(dish);

        return ResponseEntity.ok("Dish is changed");
    }
}
