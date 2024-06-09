package com.puccampinas.projectarqrestfulapi.domain.car;

import com.puccampinas.projectarqrestfulapi.dtos.car.CarDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "cars")
public class Car {

    @Id
    private String id;
    private String model;
    private Date launchDate;
    private BigDecimal price;
    private Brand brand;

    public Car(CarDTO data) {
        this.model = data.model();
        this.launchDate = data.launchDate();
        this.price = data.price();
        this.brand = data.brand();

    }
}
