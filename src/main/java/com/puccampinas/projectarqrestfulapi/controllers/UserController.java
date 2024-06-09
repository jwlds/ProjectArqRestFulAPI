package com.puccampinas.projectarqrestfulapi.controllers;

import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import com.puccampinas.projectarqrestfulapi.dtos.car.CarDTO;
import com.puccampinas.projectarqrestfulapi.dtos.user.UserUpdateDTO;
import com.puccampinas.projectarqrestfulapi.services.UserService;
import com.puccampinas.projectarqrestfulapi.domain.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> retrieveCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/")
    public ResponseEntity<UserUpdateDTO> modifyUser(@RequestBody UserUpdateDTO userUpdateDTO, @AuthenticationPrincipal User currentUser) {
        User user = findUserById(currentUser.getId());
        UserUpdateDTO updatedUser = userService.updateExistingUser(user, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteUserAccount(@AuthenticationPrincipal User currentUser) {
        userService.removeUser(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{carId}/car")
    public ResponseEntity<Car> addCarToUserAccount(@PathVariable String carId,@AuthenticationPrincipal User user) {
        Car car = userService.associateCarWithUser(user.getId(), carId);
        return ResponseEntity.ok(car);
    }

    @DeleteMapping("/{carId}/car/")
    public ResponseEntity<Void> removeCarFromUserAccount(@AuthenticationPrincipal User user, @PathVariable String carId) {
        userService.disassociateCarFromUser(user.getId(), carId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/car")
    public ResponseEntity<List<Car>> retrieveUserCars(@AuthenticationPrincipal User user) {
        List<Car> cars = userService.retrieveUserCars(user.getId());
        return ResponseEntity.ok(cars);
    }

    private User findUserById(String userId) {
        try {
            return userService.findUserById(userId);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }
}
