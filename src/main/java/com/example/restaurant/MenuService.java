package com.example.restaurant;

import com.example.restaurant.dto.MenuDto;
import com.example.restaurant.entity.Menu;
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
public class MenuService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public MenuDto create(Long restId, MenuDto dto) {
        throw new RuntimeException("not implemented");
    }

    public List<MenuDto> readAll(Long restId) {
        throw new RuntimeException("not implemented");
    }

    public MenuDto read(Long restId, Long menuId) {
        throw new RuntimeException("not implemented");
    }

    public MenuDto update(Long restId, Long menuId, MenuDto dto) {
        throw new RuntimeException("not implemented");
    }

    public void delete(Long restId, Long menuId) {
        throw new RuntimeException("not implemented");
    }
}
