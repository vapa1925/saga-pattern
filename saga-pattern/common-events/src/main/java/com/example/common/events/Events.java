package com.example.common.events;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class OrderCreatedEvent {
    private String orderId;
    private double amount;
}

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentCompletedEvent { private String orderId; }

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentFailedEvent { private String orderId; }

@Data @NoArgsConstructor @AllArgsConstructor
public class InventoryUpdatedEvent { private String orderId; }

@Data @NoArgsConstructor @AllArgsConstructor
public class InventoryFailedEvent { private String orderId; }