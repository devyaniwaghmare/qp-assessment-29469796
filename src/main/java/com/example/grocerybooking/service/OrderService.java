package com.example.grocerybooking.service;

import com.example.grocerybooking.dto.OrderItemDto;
import com.example.grocerybooking.model.GroceryItem;
import com.example.grocerybooking.model.Order;
import com.example.grocerybooking.model.OrderItem;
import com.example.grocerybooking.model.User;
import com.example.grocerybooking.repository.GroceryItemRepository;
import com.example.grocerybooking.repository.OrderRepository;
import com.example.grocerybooking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private GroceryItemRepository groceryItemRepository;
    private UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        GroceryItemRepository groceryItemRepository,
                        UserRepository userRepository)
    {
        this.orderRepository = orderRepository;
        this.groceryItemRepository = groceryItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createOrder(String username, List<OrderItemDto> orderItemDtos) {
        {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("User not found.");
            }
            Order order = new Order();
            order.setUser(user);
            order.setLocalDateTime(LocalDateTime.now());

            List<OrderItem> orderItems = orderItemDtos.stream().map(
                    dto -> {
                        Optional<GroceryItem> groceryItem = groceryItemRepository.findById(dto.getGroceryItemId());
                        if (!groceryItem.isPresent() || groceryItem.get().isDeleted()) {
                            throw new RuntimeException("Grocery Item not found");
                        }
                        if (groceryItem.get().getInventory() < dto.getQuantity()) {
                            throw new RuntimeException("Insufficient inventory for item: " + groceryItem.get().getName());
                        }

                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order);
                        orderItem.setItem(groceryItem.get());
                        orderItem.setQuantity(dto.getQuantity());

                        order.setTotalPrice(order.getTotalPrice() + (dto.getQuantity() * groceryItem.get().getPrice()));

                        groceryItem.get().setInventory(groceryItem.get().getInventory() - dto.getQuantity());
                        groceryItemRepository.save(groceryItem.get());
                        return orderItem;

                    }
            ).collect(Collectors.toList());
            order.setOrderItems(orderItems);
            return orderRepository.save(order);
        }
    }

}
