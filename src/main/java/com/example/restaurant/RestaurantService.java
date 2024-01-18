package com.example.restaurant;

import com.example.restaurant.dto.RestaurantDto;
import com.example.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;

    public RestaurantDto create(RestaurantDto dto) {
        throw new RuntimeException("not implemented");
    }

    public List<RestaurantDto> readAll() {
        throw new RuntimeException("not implemented");
    }

    public RestaurantDto read(Long id) {
        throw new RuntimeException("not implemented");
    }

    public RestaurantDto update(Long id, RestaurantDto dto) {
        throw new RuntimeException("not implemented");
    }

    public void delete(Long id) {
        throw new RuntimeException("not implemented");
    }
}
