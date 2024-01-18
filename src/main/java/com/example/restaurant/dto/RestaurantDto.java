package com.example.restaurant.dto;

import com.example.restaurant.entity.Restaurant;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    @Setter
    private String name;
    @Setter
    private String category;
    @Setter
    private Integer openHour;
    @Setter
    private Integer closeHour;

    public static RestaurantDto fromEntity(Restaurant entity) {
        return new RestaurantDto(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getOpenHours(),
                entity.getCloseHours()
        );
    }
}
