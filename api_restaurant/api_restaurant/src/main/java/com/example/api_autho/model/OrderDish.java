package com.example.api_autho.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@Table(name = "order_dish")
@EntityListeners(AuditingEntityListener.class)
public class OrderDish {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "dish_id")
    private Long dishId;

    @Column(name = "price")
    private int price;

    @Column(name = "quantity")
    private int quantity;
}
