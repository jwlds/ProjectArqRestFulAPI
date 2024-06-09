package com.puccampinas.projectarqrestfulapi.services;

import com.puccampinas.projectarqrestfulapi.domain.car.Brand;
import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import com.puccampinas.projectarqrestfulapi.dtos.car.CarDTO;
import com.puccampinas.projectarqrestfulapi.repositories.CarRepository;
import com.puccampinas.projectarqrestfulapi.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    @Test
    public void givenCarsInRepository_whenRetrieveAllCars_thenReturnAllCars() throws ParseException {
        Car car1 = new Car(new CarDTO("Model1", parseDate("2020-01-01"), BigDecimal.valueOf(10000), Brand.AUDI));
        Car car2 = new Car(new CarDTO("Model2", parseDate("2021-01-01"), BigDecimal.valueOf(20000), Brand.BMW));
        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<Car> result = carService.retrieveAllCars();

        assertEquals(2, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void givenCarDTO_whenFindCarOrCreateIfNotFound_thenReturnExistingCarOrCreateNew() throws ParseException {
        CarDTO carDTO = new CarDTO("Model1", parseDate("2020-01-01"), BigDecimal.valueOf(10000), Brand.AUDI);
        Car car = new Car(carDTO);
        when(carRepository.findAll()).thenReturn(Arrays.asList(car));

        Optional<Car> result = carService.findCarOrCreateIfNotFound(carDTO);

        assertEquals(Optional.of(car), result);
    }

    @Test
    public void givenCarId_whenFindCarById_thenReturnCar() throws ParseException {
        CarDTO carDTO = new CarDTO("Model1", parseDate("2020-01-01"), BigDecimal.valueOf(10000), Brand.AUDI);
        Car car = new Car(carDTO);
        when(carRepository.findById("1")).thenReturn(Optional.of(car));

        Optional<Car> result = carService.findCarById("1");

        assertEquals(Optional.of(car), result);
    }

    @Test
    public void givenCarDTO_whenCreateNewCar_thenReturnCreatedCar() throws ParseException {
        CarDTO carDTO = new CarDTO("Model1", parseDate("2020-01-01"), BigDecimal.valueOf(10000), Brand.AUDI);
        Car car = new Car(carDTO);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car result = carService.createNewCar(carDTO);

        assertEquals(car, result);
    }

    @Test
    public void givenCarIdAndCarDTO_whenModifyExistingCar_thenReturnModifiedCar() throws ParseException {
        CarDTO carDTO = new CarDTO("Model1", parseDate("2020-01-01"), BigDecimal.valueOf(10000), Brand.AUDI);
        Car car = new Car(carDTO);
        when(carRepository.findById("1")).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car result = carService.modifyExistingCar("1", carDTO);

        assertEquals(car, result);
    }

    @Test
    public void givenNonExistingCarIdAndCarDTO_whenModifyExistingCar_thenThrowException() throws ParseException {
        CarDTO carDTO = new CarDTO("Model1", parseDate("2020-01-01"), BigDecimal.valueOf(10000), Brand.AUDI);
        when(carRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carService.modifyExistingCar("1", carDTO));
    }

    @Test
    public void givenCarId_whenRemoveCar_thenCarIsRemoved() {
        doNothing().when(carRepository).deleteById("1");

        carService.removeCar("1");

        verify(carRepository, times(1)).deleteById("1");
    }
}
