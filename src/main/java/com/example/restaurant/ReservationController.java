package com.example.restaurant;

import com.example.restaurant.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/restaurant/{restId}/reservation")
@RequiredArgsConstructor
public class ReservationController {
  private final ReservationService service;

  // CREATE
  @PostMapping
  public ReservationDto create(
    @PathVariable("restId") Long restId, // PathVariable: URL 상의 변수를 찾아 넣어주는 것
    @RequestBody ReservationDto dto // RequestBody: HTTP 요청에 있는 Body 부분을 해석해서 넣어주는 것
  ) {

    return service.create(restId, dto);
  }

  // READ
  @GetMapping
  public List<ReservationDto> readAll(
    @PathVariable("restId") Long restId
  ) {
    return service.readAll(restId);
  }
}
