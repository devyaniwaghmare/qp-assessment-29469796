package com.example.grocerybooking.controller;

import com.example.grocerybooking.dto.OrderItemDto;
import com.example.grocerybooking.model.GroceryItem;
import com.example.grocerybooking.model.Order;
import com.example.grocerybooking.service.GroceryItemService;
import com.example.grocerybooking.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/user")
public class UserController {

    private OrderService orderService;
    private GroceryItemService groceryItemService;

    @Autowired
    public UserController(OrderService orderService,GroceryItemService groceryItemService)
    {
        this.orderService = orderService;
        this.groceryItemService = groceryItemService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<GroceryItem>> getGroceryItem()
    {
        List<GroceryItem> groceryItems = groceryItemService.getAllGroceryItemsUser();
        return ResponseEntity.ok(groceryItems);

    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody List<OrderItemDto> orderItemDtos, Authentication authentication)
    {
        String username = authentication.getName();
        try {
            Order order = orderService.createOrder(username, orderItemDtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
