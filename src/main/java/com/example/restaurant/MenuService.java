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
      Optional<Restaurant> optionalRestaurant
        = restaurantRepository.findById(restId);

      if (optionalRestaurant.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }

      Menu newEntity = new Menu(
        dto.getName(),
        dto.getPrice(),
        optionalRestaurant.get()
      );

      return MenuDto.fromEntity(menuRepository.save(newEntity));
    }

    public List<MenuDto> readAll(Long restId) {
        if (!restaurantRepository.existsById(restId)) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<MenuDto> menuDtoList = new ArrayList<>();
        for (Menu entity: menuRepository.findAllByRestaurantId(restId)) {
          menuDtoList.add(MenuDto.fromEntity(entity));
        }

        return menuDtoList;
    }

    public MenuDto read(Long restId, Long menuId) {
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);

        if (optionalMenu.isEmpty()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Menu menu = optionalMenu.get();
        if (!menu.getRestaurant().getId().equals(restId)) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return MenuDto.fromEntity(menu);
    }

    public MenuDto update(Long restId, Long menuId, MenuDto dto) {
      Optional<Menu> optionalMenu = menuRepository.findById(menuId);

      if (optionalMenu.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }

      Menu menu = optionalMenu.get();
      if (!menu.getRestaurant().getId().equals(restId)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      menu.setName(dto.getName());
      menu.setPrice(dto.getPrice());
      return MenuDto.fromEntity(menuRepository.save(menu));
    }

    public void delete(Long restId, Long menuId) {
      Optional<Menu> optionalMenu = menuRepository.findById(menuId);

      if (optionalMenu.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }

      Menu menu = optionalMenu.get();
      if (!menu.getRestaurant().getId().equals(restId)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      menuRepository.deleteById(restId);
    }

  // 중복된 내용 메서드로 뽑아보기 (그리 길지 않아서 굳이 메서드로 만들 필요는 없었다.)
  private Menu getMenuById(Long restId, Long menuId) {
    Optional<Menu> optionalMenu
      = menuRepository.findById(menuId);
    // 찾는 Menu가 없다면 에러 던지기
    if (optionalMenu.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    Menu menu = optionalMenu.get();
    // 찾는 Menu가 해당 레스토랑과 일치하지 않다면 에러 던지기
    if (!menu.getRestaurant().getId().equals(restId))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    return menu;
  }
}
