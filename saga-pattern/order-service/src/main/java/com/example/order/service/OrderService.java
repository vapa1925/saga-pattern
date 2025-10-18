package com.example.order.service;

import com.example.common.events.*;
import com.example.order.model.Order;
import com.example.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public String createOrder(Order order) {
        order.setStatus("PENDING");
        repo.save(order);
        kafkaTemplate.send("order-created-topic", new OrderCreatedEvent(order.getId(), order.getAmount()));
        return order.getId();
    }

    @KafkaListener(topics = "inventory-updated-topic", groupId = "order-group")
    public void onInventoryUpdated(InventoryUpdatedEvent e) {
        repo.findById(e.getOrderId()).ifPresent(o -> {
            o.setStatus("COMPLETED");
            repo.save(o);
        });
    }

    @KafkaListener(topics = {"payment-failed-topic", "inventory-failed-topic"}, groupId = "order-group")
    public void onFailure(Object e) {
        String orderId = null;
        if (e instanceof PaymentFailedEvent) orderId = ((PaymentFailedEvent) e).getOrderId();
        else if (e instanceof InventoryFailedEvent) orderId = ((InventoryFailedEvent) e).getOrderId();
        if (orderId != null)
            repo.findById(orderId).ifPresent(o -> {
                o.setStatus("CANCELLED");
                repo.save(o);
            });
    }
}