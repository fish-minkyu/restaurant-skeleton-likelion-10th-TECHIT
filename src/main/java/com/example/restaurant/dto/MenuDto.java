package com.example.restaurant.dto;

import com.example.restaurant.entity.Menu;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private Long id;
    @Setter
    private String name;
    @Setter
    private Integer price;

    public static MenuDto fromEntity(Menu entity) {
        return new MenuDto(
                entity.getId(),
                entity.getName(),
                entity.getPrice()
        );
    }
}
