package com.example.controller;


import com.example.entities.Item;
import com.example.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public Page<Item> getItems(@RequestParam(required = false) Long categoryId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "50") int size) {
        if(categoryId != null){
            return itemRepository.findByCategoryId(categoryId, PageRequest.of(page, size));
        }
        return itemRepository.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable Long id) {
        return itemRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        Item existing = itemRepository.findById(id).orElseThrow();
        existing.setSku(item.getSku());
        existing.setName(item.getName());
        existing.setPrice(item.getPrice());
        existing.setStock(item.getStock());
        existing.setCategory(item.getCategory());
        return itemRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }
}
