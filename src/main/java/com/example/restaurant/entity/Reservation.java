package com.example.restaurant.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  private Integer date;
  @Setter
  private Integer reserveHour; // 예약 시간
  @Setter
  private Integer people; // 예약 인원 수
  @Setter
  private Integer duration; // 총 머물 시간

  @ManyToOne
  private Restaurant restaurant;

  public Reservation(
    Integer date,
    Integer reserveHour,
    Integer people,
    Integer duration,
    Restaurant restaurant
  ) {
    this.date = date;
    this.reserveHour = reserveHour;
    this.people = people;
    this.duration = duration;
    this.restaurant = restaurant;
  }
}
