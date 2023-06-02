package com.example.api_autho.rest;

import com.example.api_autho.dto.MakeOrderDto;
import com.example.api_autho.dto.ShowOrderDto;
import com.example.api_autho.model.DishQuantity;
import com.example.api_autho.model.Order;
import com.example.api_autho.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/order/")
public class OrdersRestController {
    private final OrderService orderService;

    @PostMapping("make")
    public ResponseEntity showOrder(@RequestBody MakeOrderDto makeOrderDto) throws InterruptedException, SQLException {
        if (orderService.checkDishes(makeOrderDto.getDishes())) {
            return new ResponseEntity<>("No such dishes in menu", HttpStatus.BAD_REQUEST);
        }

        DishQuantity dish = orderService.checkAmount(makeOrderDto.getDishes());
        if (dish != null) {
            return new ResponseEntity<>("No needed amount of " + dish.dish + " in menu", HttpStatus.BAD_REQUEST);
        }

        Order newOrder = new Order();
        newOrder.setUserId(makeOrderDto.getId());
        newOrder.setSpecialRequests(makeOrderDto.getSpecialties());
        newOrder.setStatus(makeOrderDto.getStatus());

        orderService.addOrder(newOrder);

        orderService.addOrderDish(newOrder, makeOrderDto.getDishes());

        return new ResponseEntity<>("Order registered successfully", HttpStatus.OK);
    }

    @GetMapping("show")
    public ResponseEntity showOrder(@RequestBody ShowOrderDto showOrderDto) {
        List<DishQuantity> dishes = orderService.findDishes(showOrderDto.getId());

        String status = orderService.getOrderStatus(showOrderDto.getId());

        Map<Object, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("dishes", dishes);


        return ResponseEntity.ok(response);
    }
}
