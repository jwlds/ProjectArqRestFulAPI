package com.puccampinas.projectarqrestfulapi.dtos.car;

import com.puccampinas.projectarqrestfulapi.domain.car.Brand;

import java.math.BigDecimal;
import java.util.Date;

public record CarDTO(String model, Date launchDate, BigDecimal price, Brand brand) {
}
