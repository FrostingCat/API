package com.example.api_autho.service.impl;

import com.example.api_autho.model.Dish;
import com.example.api_autho.model.DishQuantity;
import com.example.api_autho.model.Order;
import com.example.api_autho.model.OrderDish;
import com.example.api_autho.repository.DishRepository;
import com.example.api_autho.repository.OrderDishRepository;
import com.example.api_autho.repository.OrderRepository;
import com.example.api_autho.service.OrderProcessorService;
import com.example.api_autho.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderDishRepository orderDishRepository;

    private final DishRepository dishRepository;

    @Override
    public void addOrder(Order order) {
        Order registeredOrder = orderRepository.save(order);

        log.info("IN addOrder - order: {} successfully added", registeredOrder);
    }
    @Override
    public void addNewDish(Dish dish) {
        Dish newDish = dishRepository.save(dish);

        log.info("IN addNewDish - dish: {} successfully added", newDish);
    }

    @Override
    public void addOrderDish(Order order, List<DishQuantity> dishes) throws InterruptedException, SQLException {
        for (DishQuantity dish : dishes) {
            OrderDish newOrderDish = new OrderDish();
            newOrderDish.setOrderId(order.getId());
            Dish newDish = dishRepository.findByName(dish.dish);
            newOrderDish.setDishId(newDish.getId());
            newOrderDish.setQuantity(dish.quantity);
            newOrderDish.setPrice(newDish.getPrice());
            orderDishRepository.save(newOrderDish);
            log.info("IN addOrderDish - newOrderDish: {} successfully added", newOrderDish);
        }
        changeDishAmount(dishes);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(OrderProcessorService::processOrders, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void deleteDish(String name) {
        try {
            String url = "jdbc:mysql://localhost:3306/restaurant";
            String username = "root";
            String password = "Db954216837";
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement st = connection.prepareStatement("DELETE FROM dish WHERE name = ?");
            st.setString(1, name);
            st.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        log.info("IN deleteDish: {} deleted", name);
    }

    @Override
    public void changeDish(Dish dish) {
        String url = "jdbc:mysql://localhost:3306/restaurant";
        String username = "root";
        String password = "Db954216837";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (dish.getDescription() != null) {
                String sql = "UPDATE dish SET description = ? WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, dish.getDescription());
                statement.setString(2, dish.getName());
                statement.executeUpdate();
            }
            if (dish.getPrice() != 0) {
                String sql = "UPDATE dish SET price = ? WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, String.valueOf(dish.getPrice()));
                statement.setString(2, dish.getName());
                statement.executeUpdate();
            }
            if (dish.getQuantity() != 0) {
                String sql = "UPDATE dish SET quantity = ? WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, String.valueOf(dish.getQuantity()));
                statement.setString(2, dish.getName());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean checkDishes(List<DishQuantity> dishes) {
        for (DishQuantity dish : dishes) {
            if (dishRepository.findByName(dish.dish) == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DishQuantity checkAmount(List<DishQuantity> dishes) {
        for (DishQuantity dish : dishes) {
            int quantity = dishRepository.findByName(dish.dish).getQuantity();
            if (quantity < dish.quantity)
                return dish;
        }
        return null;
    }

    @Override
    public List<DishQuantity> findDishes(Long id) {
        List<DishQuantity> res = new ArrayList<>();
        List<OrderDish> dishes = orderDishRepository.findAll();
        for (OrderDish dish : dishes) {
            if (!Objects.equals(dish.getOrderId(), id)) {
                continue;
            }
            log.info("IN dish: {}", dish);
            DishQuantity dishQuantity = new DishQuantity();
            dishQuantity.dish = dishRepository.getById(dish.getDishId()).getName();
            dishQuantity.quantity = dish.getQuantity();
            res.add(dishQuantity);
        }
        return res;
    }

    @Override
    public String getOrderStatus(Long id) {
        return orderRepository.getById(id).getStatus();
    }

    @Override
    public List<Dish> findDishesMenu() {
        List<Dish> dishes = dishRepository.findAll();
        dishes.removeIf(dish -> dish.getQuantity() == 0);
        return dishes;
    }

    @Override
    public boolean checkManager(Long id) {
        String url = "jdbc:mysql://localhost:3306/users_db";
        String username = "root";
        String password = "Db954216837";
        String role = null;

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            PreparedStatement statement = connection.prepareStatement("select * from user where id = ?");
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            role = resultSet.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !Objects.equals(role, "manager");
    }

    private void changeDishAmount(List<DishQuantity> dishes) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "Db954216837")) {
            String sql = "UPDATE dish SET quantity = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (DishQuantity dish : dishes) {
                int quantity = dishRepository.findByName(dish.dish).getQuantity();
                statement.setString(1, String.valueOf(quantity - dish.quantity));
                statement.setString(2, dish.dish);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
