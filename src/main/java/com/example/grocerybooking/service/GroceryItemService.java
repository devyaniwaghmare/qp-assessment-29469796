package com.example.grocerybooking.service;

import com.example.grocerybooking.model.GroceryItem;
import com.example.grocerybooking.repository.GroceryItemRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroceryItemService {


    private GroceryItemRepository groceryItemRepository;

    @Autowired
    public GroceryItemService(GroceryItemRepository groceryItemRepository)
    {
        this.groceryItemRepository = groceryItemRepository;
    }

    public GroceryItem addGroceryItem(GroceryItem item) {
        return groceryItemRepository.save(item);
    }

    public List<GroceryItem> getAllGroceryItemsUser() {
        List<GroceryItem> groceryItems = groceryItemRepository.findAll();
        return groceryItems.stream().filter(item -> item.getInventory()>0 && !item.isDeleted()).collect(Collectors.toList());
    }

    public List<GroceryItem> getAllGroceryItems() {
        List<GroceryItem> groceryItems = groceryItemRepository.findAll();
        return groceryItems;
    }

    public Optional<GroceryItem> getGroceryItemById(Long id) {
        return groceryItemRepository.findById(id);
    }

    public GroceryItem updateGroceryItem(Long id, GroceryItem item) {
        Optional<GroceryItem> existingItem = groceryItemRepository.findById(id);
        if (existingItem.isPresent()) {
            GroceryItem updatedItem = existingItem.get();
            updatedItem.setName(item.getName());
            updatedItem.setPrice(item.getPrice());
            updatedItem.setInventory(item.getInventory());
            return groceryItemRepository.save(updatedItem);
        } else {
            return null;
        }
    }

    public void deleteGroceryItem(Long id) {
        Optional<GroceryItem> groceryItem = groceryItemRepository.findById(id);
        if(!groceryItem.isPresent())
        {
            throw new RuntimeException("Grocery item not present in system.");
        }
        groceryItem.get().setDeleted(true);
        groceryItemRepository.save(groceryItem.get());
    }

    public GroceryItem manageInventory(Long id, int inventory, String inventoryOperation) {
        Optional<GroceryItem> existingItem = groceryItemRepository.findById(id);
        if (existingItem.isPresent()) {
            GroceryItem groceryItem = existingItem.get();
            if(inventoryOperation.toUpperCase().equals("ADD"))
            {
                groceryItem.setInventory(groceryItem.getInventory()+inventory);
            }
            else{
                if(groceryItem.getInventory()-inventory >= 0)
                {
                    groceryItem.setInventory(groceryItem.getInventory()-inventory);
                }
            }
            return groceryItemRepository.save(groceryItem);
        } else {
            return null;
        }
    }

    public List<GroceryItem> getItems()
    {
        List<GroceryItem> groceryItems = groceryItemRepository.findAll();
        List<GroceryItem> avaGroceryItem = groceryItems.stream().filter(item -> item.getInventory() > 0).collect(Collectors.toList());
        return avaGroceryItem;
    }

    public GroceryItem makeGroceryItemAva(Long id)
    {
        Optional<GroceryItem> groceryItem = groceryItemRepository.findById(id);
        if(!groceryItem.isPresent())
        {
            throw new RuntimeException("Grocery Item with Id : "+ id +" not found in system.");
        }
        else if(!groceryItem.get().isDeleted()){
            throw new RuntimeException("Grocery Item is already available.");
        }
        groceryItem.get().setDeleted(false);
        return groceryItemRepository.save(groceryItem.get());
    }

}
