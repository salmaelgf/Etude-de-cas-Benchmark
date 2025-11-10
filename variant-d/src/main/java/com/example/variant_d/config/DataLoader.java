package com.example.variant_d.config;


import com.example.variant_d.entities.Category;
import com.example.variant_d.entities.Item;
import com.example.variant_d.repository.CategoryRepository;
import com.example.variant_d.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public DataLoader(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() > 0) return;

        List<Category> cats = new ArrayList<>();
        IntStream.rangeClosed(1, 2000).forEach(i -> {
            Category c = new Category();
            c.setCode(String.format("CAT%04d", i));
            c.setName("Category " + i);
            cats.add(c);
        });
        categoryRepository.saveAll(cats);

        List<Item> items = new ArrayList<>();
        long skuCounter = 1L;
        for (Category cat : cats) {
            for (int j = 0; j < 50; j++) {
                Item it = new Item();
                it.setSku(String.format("SKU%06d", skuCounter++));
                it.setName("Item " + it.getSku());
                it.setPrice(BigDecimal.valueOf(10 + (skuCounter % 100)));
                it.setStock(100);
                it.setCategory(cat);
                items.add(it);
            }
        }
        itemRepository.saveAll(items);
    }
}
