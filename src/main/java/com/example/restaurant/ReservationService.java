package com.example.restaurant;

import com.example.restaurant.dto.ReservationDto;
import com.example.restaurant.dto.RestaurantDto;
import com.example.restaurant.entity.Reservation;
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
public class ReservationService {
  private final RestaurantRepository restaurantRepository;
  private final ReservationRepository reservationRepository;

  // CREATE
  public ReservationDto create(Long restId, ReservationDto dto) {
    // 식당 정보를 조회한다.
    // findById는 기본적으로 Optional을 반환한다.
    Optional<Restaurant> optionalRestaurant
      = restaurantRepository.findById(restId);

    // 만약 없는 식당이라면 사용자에게 에러를 반환한다.
    if (optionalRestaurant.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 1. 식당이 여는 시간에 예약을 요청했는지 판단한다.
    Restaurant restaurant = optionalRestaurant.get();

    // 오픈 시간 <= 요청 시간
    // restaurant.getOpenHours() <= dto.getReserveHour()이면 create 가능
    if (restaurant.getOpenHours() > dto.getReserveHour()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // 닫는 시간 > 요청 시간
    // restaurant.getCloseHours() > dto.getReserveHour()이면 create 가능
    if (restaurant.getCloseHours() < dto.getReserveHour()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // 2. 식당이 닫기 전에 떠난다.
    // 요청 시간 + 머무는 시간 <= 닫는 시간
    // dto.getReserveHour() + dto.getDuration() <= restaurant.getCloseHours();이면 create 가능
    if (dto.getReserveHour() + dto.getDuration() > restaurant.getCloseHours()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // 3. 같은 일자, 예약 시간엔 추가 예약이 불가하다.


    Reservation newEntity = new Reservation(
      dto.getDate(),
      dto.getReserveHour(),
      dto.getPeople(),
      dto.getDuration(),
      restaurant
    );

    return ReservationDto.fromEntity(reservationRepository.save(newEntity));
  }


  // READ
  public List<ReservationDto> readAll(Long restId) {
    List<ReservationDto> reservationDtoList = new ArrayList<>();
    for (Reservation entity: reservationRepository.findAllByRestaurantId(restId)) {
      reservationDtoList.add(ReservationDto.fromEntity(entity));
    }

    return reservationDtoList;
  }

  public ReservationDto read() {
    throw new Error("later");
  }

  // UPDATE
  public RestaurantDto update() {
    throw new Error("later");
  }


  // DELETE
  public void delete() {

    throw new Error("later");
  }
}
