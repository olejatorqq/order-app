package com.taskmanagment.orderservice.repository;

import com.taskmanagment.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}