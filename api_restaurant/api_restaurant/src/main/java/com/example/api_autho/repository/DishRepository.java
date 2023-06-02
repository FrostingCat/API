package com.example.api_autho.repository;

import com.example.api_autho.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    Dish findByName(String name);
}
