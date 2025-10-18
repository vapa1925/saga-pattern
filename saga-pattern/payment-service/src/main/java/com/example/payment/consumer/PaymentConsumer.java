package com.example.payment.consumer;

import com.example.common.events.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Random rnd = new Random();

    @KafkaListener(topics = "order-created-topic", groupId = "payment-group")
    public void handleOrderCreated(OrderCreatedEvent e) {
        boolean success = rnd.nextBoolean();
        if (success)
            kafkaTemplate.send("payment-success-topic", new PaymentCompletedEvent(e.getOrderId()));
        else
            kafkaTemplate.send("payment-failed-topic", new PaymentFailedEvent(e.getOrderId()));
    }

    @KafkaListener(topics = "inventory-failed-topic", groupId = "payment-group")
    public void handleInventoryFailed(InventoryFailedEvent e) {
        System.out.println("[PaymentService] refunding for order: " + e.getOrderId());
    }
}