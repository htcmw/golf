package com.reborn.golf.controller;

import com.reborn.golf.service.PurchasedItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/sales")
@Log4j2
@RequiredArgsConstructor
public class PurchasedItemsController {

    private final PurchasedItemsService purchasedItemsService;

    @GetMapping
    public ResponseEntity read(){
        return null;
    }

    @PostMapping
    public ResponseEntity register(){
        return null;
    }

    @PutMapping
    public ResponseEntity modify(){
        return null;
    }

    @DeleteMapping
    public ResponseEntity delete(){
        return null;
    }
}
