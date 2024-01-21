# Restaurant-Skeleton
- 2024.01.19 09시 ~ 13시

REST란 가이드라인을 연습을 하기 위해 프로젝트를 만들었다.  
레스토랑 프로젝트는 Restaurant, Menu, Reservatoin 총 3개의 테이블로 이루어져 있으며  
어느 분야의 식당, 메뉴, 예약을 할 수 있다.  
단, 예약은 영업 시간 내, 동일한 날짜의 시간대에 예약이 없어야지만 가능하다.

또한, 기존엔 안해주었던 에러 처리방식을 서비스 계층에서 구현하였으며  
새로운 문법, Steam, Optional Method와 심화 CRUD 로직이 있다.

## 스택

- Spring Boot 3.2.1
- Spring Boot Data JPA
- Lombok
- SQLite

## keyPoint
[stream](src/main/java/com/example/restaurant/RestaurantService.java)
```java
      return repository.findAll().stream()
        // .map(entity -> RestaurantDto.fromEntity(entity))
        .map(RestaurantDto::fromEntity)
        .collect(Collectors.toList());
```

[Optional.map(), Optional.orElseThrow()](src/main/java/com/example/restaurant/RestaurantService.java)
```java
    public RestaurantDto read(Long id) {
        // 이때까지 해왔던 기존 방식
/*        Optional<Restaurant> optionalRestaurant = repository.findById(id);

        if (optionalRestaurant.isEmpty()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return RestaurantDto.fromEntity(optionalRestaurant.get());*/

        // Optional 메소드를 이용한 방식
      // Optional.map(), Optional.orElseThrow()
        return repository.findById(id)
          // .map(entity -> RestaurantDto.fromEntity(entity))
          .map(RestaurantDto::fromEntity)
          // orElstThrow의 기본 에러 옵션은 500 에러 옵션이다.
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
```

[중복 코드 메서드로 뽑아내기](src/main/java/com/example/restaurant/MenuService.java)
```java
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
```

[예약 기능](src/main/java/com/example/restaurant/ReservationService.java)  
조건 1번
```java
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
```

조건 2번
```java
    // 2. 식당이 닫기 전에 떠난다.
    // 요청 시간 + 머무는 시간 <= 닫는 시간
    // dto.getReserveHour() + dto.getDuration() <= restaurant.getCloseHours();이면 create 가능
    if (dto.getReserveHour() + dto.getDuration() > restaurant.getCloseHours()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
```

조건 3번
```java
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
```

## 복습

~~2024년 1월 21일 복습 완료!~~