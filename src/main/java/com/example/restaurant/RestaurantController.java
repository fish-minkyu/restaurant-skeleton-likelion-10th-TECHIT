package com.example.restaurant;

import com.example.restaurant.dto.RestaurantDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

    @PostMapping
    public RestaurantDto create(
            @RequestBody
            RestaurantDto dto
    ) {
        throw new RuntimeException("not implemented");
    }

    @GetMapping
    public List<RestaurantDto> readAll() {
        throw new RuntimeException("not implemented");
    }

    @GetMapping("/{id}")
    public RestaurantDto read(
            @PathVariable("id")
            Long id
    ) {
        throw new RuntimeException("not implemented");
    }

    @PutMapping("/{id}")
    public RestaurantDto update(
            @PathVariable("id")
            Long id,
            @RequestBody
            RestaurantDto dto
    ) {
        throw new RuntimeException("not implemented");
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id")
            Long id
    ) {
        throw new RuntimeException("not implemented");
    }
}
