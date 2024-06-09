package com.puccampinas.projectarqrestfulapi.services;

import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import com.puccampinas.projectarqrestfulapi.dtos.car.CarDTO;
import com.puccampinas.projectarqrestfulapi.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> retrieveAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> findCarOrCreateIfNotFound(CarDTO carDTO) {
        return carRepository.findAll().stream()
                .filter(car -> isSameCar(car, carDTO))
                .findFirst();
    }

    public Optional<Car> findCarById(String id) {
        return carRepository.findById(id);
    }

    public Car createNewCar(CarDTO carDTO) {
        Car car = new Car(carDTO);
        return carRepository.save(car);
    }

    public Car modifyExistingCar(String id, CarDTO carDTO) {
        Car car = findCarById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        updateCarDetails(car, carDTO);
        return carRepository.save(car);
    }

    public void removeCar(String id) {
        carRepository.deleteById(id);
    }



    private boolean isSameCar(Car car, CarDTO carDTO) {
        return car.getModel().equals(carDTO.model())
                && car.getLaunchDate().equals(carDTO.launchDate())
                && Objects.equals(car.getPrice(), carDTO.price())
                && car.getBrand().equals(carDTO.brand());
    }

    private void updateCarDetails(Car car, CarDTO carDTO) {
        car.setModel(carDTO.model());
        car.setLaunchDate(carDTO.launchDate());
        car.setPrice(carDTO.price());
        car.setBrand(carDTO.brand());
    }
}
