package com.example.api_autho.service;

import com.example.api_autho.model.Dish;
import com.example.api_autho.model.DishQuantity;
import com.example.api_autho.model.Order;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface OrderService {
    void addOrder(Order order);

    void addNewDish(Dish dish);

    void addOrderDish(Order order, List<DishQuantity> dishes) throws InterruptedException, SQLException;

    void deleteDish(String name);

    void changeDish(Dish dish);

    boolean checkDishes(List<DishQuantity> dishes);

    DishQuantity checkAmount(List<DishQuantity> dishes);

    List<DishQuantity> findDishes(Long id);

    String getOrderStatus(Long id);

    List<Dish> findDishesMenu();

    boolean checkManager(Long id);
}
