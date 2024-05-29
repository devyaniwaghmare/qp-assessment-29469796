package com.example.grocerybooking.controller;

import com.example.grocerybooking.dto.InventoryDto;
import com.example.grocerybooking.model.GroceryItem;
import com.example.grocerybooking.service.GroceryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/grocery")
public class AdminController {

    @Autowired
    private GroceryItemService groceryItemService;


    @PostMapping("/add")
    public ResponseEntity<GroceryItem> addGroceryItem(@RequestBody GroceryItem item)
    {
        return ResponseEntity.ok(groceryItemService.addGroceryItem(item));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroceryItem>>  getAllGroceryItems() {
        return ResponseEntity.ok(groceryItemService.getAllGroceryItems());
    }

    @GetMapping("/get/{itemId}")
    public ResponseEntity<GroceryItem> getGroceryItemById(@PathVariable Long itemId)
    {
        Optional<GroceryItem> item = groceryItemService.getGroceryItemById(itemId);
        if(item.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item.get());
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Void> deleteGroceryItem(@PathVariable Long itemId)
    {
        groceryItemService.deleteGroceryItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<GroceryItem> updateGroceryItem(@PathVariable Long itemId, @RequestBody GroceryItem item)
    {
        GroceryItem updatedItem = groceryItemService.updateGroceryItem(itemId, item);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<GroceryItem> manageInventory(@PathVariable Long id, @RequestBody InventoryDto inventory) {
        GroceryItem updatedItem = groceryItemService.manageInventory(id, inventory.getQuantity(),inventory.getInventoryOperation());
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/makeAva/{id}")
    public ResponseEntity<GroceryItem> makeGroceryItemAva(@PathVariable Long id)
    {
        GroceryItem groceryItem = groceryItemService.makeGroceryItemAva(id);
        return ResponseEntity.ok(groceryItem);
    }


}
