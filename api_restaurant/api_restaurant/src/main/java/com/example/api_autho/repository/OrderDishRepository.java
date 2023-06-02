package com.example.api_autho.repository;

import com.example.api_autho.model.DishQuantity;
import com.example.api_autho.model.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {
}
