package com.example.grocerybooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "grocery_item_id")
    private GroceryItem item;

    private int quantity;

}
