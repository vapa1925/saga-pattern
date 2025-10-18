package com.example.inventory.consumer;

import com.example.common.events.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class InventoryConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Random rnd = new Random();

    @KafkaListener(topics = "payment-success-topic", groupId = "inventory-group")
    public void handlePaymentCompleted(PaymentCompletedEvent e) {
        boolean ok = rnd.nextBoolean();
        if (ok)
            kafkaTemplate.send("inventory-updated-topic", new InventoryUpdatedEvent(e.getOrderId()));
        else
            kafkaTemplate.send("inventory-failed-topic", new InventoryFailedEvent(e.getOrderId()));
    }
}