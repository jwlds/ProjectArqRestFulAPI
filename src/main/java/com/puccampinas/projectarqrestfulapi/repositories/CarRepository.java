package com.puccampinas.projectarqrestfulapi.repositories;

import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarRepository extends MongoRepository<Car, String> {

}
