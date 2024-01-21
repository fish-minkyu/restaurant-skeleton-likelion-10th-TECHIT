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
import java.util.Arrays;
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
    // 우선 날짜 기준으로 예약 정보 조회
    List<Reservation> dateReserves =
      reservationRepository.findAllByRestaurantIdAndDate(restId, dto.getDate());
    // 24시간 중 예약된 시간을 파악하기 위한 boolean[]
    boolean[] reservable = new boolean[24];
    // 예약 가능으로 채워넣고(true)
    Arrays.fill(reservable, true);
    // 영업 시간 내에만 예약 가능하다 설정
    // Ex) 영업시간 17시 ~ 23시이면 16시까지와 24시는 false로 재할당
    for (int i = 0; i < restaurant.getOpenHours(); i++) {
      reservable[i] = false;
    }

    for (int i = restaurant.getCloseHours(); i < 24; i++) {
      reservable[i] = false;
    }

    // 각각 예약 정보를 바탕으로
    for (Reservation entity: dateReserves) {
      // 시간단위 예약 불가능 정보를 갱신
      for (int i = 0; i < entity.getDuration(); i++) {
        reservable[entity.getReserveHour() + i] = false;
      }
    }

    // 이번 예약 정보를 바탕으로
    for (int i = 0; i < dto.getDuration(); i++) {
      // 만약 예약하려는 시간대가 이미 예약 되었다면
      if (reservable[dto.getReserveHour() + i])
        // 예약 불가능
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // 예약이 가능하다면 새 예약을 받을 객체를 준비한다.
    Reservation newEntity = new Reservation(
      dto.getDate(),
      dto.getReserveHour(),
      dto.getPeople(),
      dto.getDuration(),
      restaurant
    );

    // 저장한 결과를 바탕으로 응답을 반환해준다.
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
}
