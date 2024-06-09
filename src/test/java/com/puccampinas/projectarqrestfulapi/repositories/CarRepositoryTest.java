package com.puccampinas.projectarqrestfulapi.repositories;

import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import com.puccampinas.projectarqrestfulapi.domain.car.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;


    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    @Test
    public void givenCar_whenSave_thenFindById() throws ParseException {
        // Arrange
        Car car = new Car();
        car.setModel("Model1");
        car.setLaunchDate(parseDate("2020-01-01"));
        car.setPrice(BigDecimal.valueOf(10000));
        car.setBrand(Brand.AUDI);

        // Act
        Car savedCar = carRepository.save(car);

        // Assert
        Optional<Car> retrievedCar = carRepository.findById(savedCar.getId());
        assertTrue(retrievedCar.isPresent());
        assertEquals("Model1", retrievedCar.get().getModel());
    }


    @Test
    public void givenCar_whenDelete_thenRemoveCar() throws ParseException {
        // Arrange
        Car car = new Car();
        car.setModel("Model1");
        car.setLaunchDate(parseDate("2020-01-01"));
        car.setPrice(BigDecimal.valueOf(10000));
        car.setBrand(Brand.AUDI);

        Car savedCar = carRepository.save(car);

        // Act
        carRepository.deleteById(savedCar.getId());

        // Assert
        Optional<Car> retrievedCar = carRepository.findById(savedCar.getId());
        assertFalse(retrievedCar.isPresent());
    }
}
