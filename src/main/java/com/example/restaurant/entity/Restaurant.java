package com.example.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;
    @Setter
    private String category;
    @Setter
    private Integer openHours;
    @Setter
    private Integer closeHours;

    public Restaurant(String name, String category, Integer openHours, Integer closeHours) {
        this.name = name;
        this.category = category;
        this.openHours = openHours;
        this.closeHours = closeHours;
    }
}
