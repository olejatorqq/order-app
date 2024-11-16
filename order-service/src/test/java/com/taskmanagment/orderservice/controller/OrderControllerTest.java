package com.taskmanagment.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagment.orderservice.dto.OrderDTO;
import com.taskmanagment.orderservice.model.Order;
import com.taskmanagment.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // Отключаем фильтры безопасности
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createOrder_Success() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(1L)
                .product("Product Name")
                .quantity(2)
                .price(100.0)
                .build();

        Order order = Order.builder()
                .id(1L)
                .userId(orderDTO.getUserId())
                .product(orderDTO.getProduct())
                .quantity(orderDTO.getQuantity())
                .price(orderDTO.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(orderService.createOrder(Mockito.any(OrderDTO.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.product").value("Product Name"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    // Добавьте дополнительные тесты для негативных сценариев
}
