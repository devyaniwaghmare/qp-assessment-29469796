package com.example.grocerybooking.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long groceryItemId;
    private int quantity;
}
