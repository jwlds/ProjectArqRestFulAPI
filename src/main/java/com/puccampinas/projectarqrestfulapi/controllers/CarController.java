package com.puccampinas.projectarqrestfulapi.controllers;

import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import com.puccampinas.projectarqrestfulapi.dtos.car.CarDTO;
import com.puccampinas.projectarqrestfulapi.services.CarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/car")
@SecurityRequirement(name = "bearer-key")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/")
    public ResponseEntity<List<Car>> retrieveAllCars() {
        List<Car> cars = carService.retrieveAllCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> retrieveCarById(@PathVariable String id) {
        Car car = carService.findCarById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        return ResponseEntity.ok(car);
    }

    @PostMapping("/")
    public ResponseEntity<Car> createNewCar(@RequestBody CarDTO carDTO) {
        Car car = carService.createNewCar(carDTO);
        return ResponseEntity.ok(car);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> modifyExistingCar(@PathVariable String id, @RequestBody CarDTO carDTO) {
        Car car = carService.modifyExistingCar(id, carDTO);
        return ResponseEntity.ok(car);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCar(@PathVariable String id) {
        carService.removeCar(id);
        return ResponseEntity.noContent().build();
    }
}
