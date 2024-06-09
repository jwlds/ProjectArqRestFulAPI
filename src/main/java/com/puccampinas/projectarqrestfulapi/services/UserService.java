package com.puccampinas.projectarqrestfulapi.services;

import com.puccampinas.projectarqrestfulapi.domain.car.Car;
import com.puccampinas.projectarqrestfulapi.dtos.car.CarDTO;
import com.puccampinas.projectarqrestfulapi.dtos.user.UserUpdateDTO;
import com.puccampinas.projectarqrestfulapi.repositories.UserRepository;
import com.puccampinas.projectarqrestfulapi.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarService carService;



    public User findUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User verifyUserExists(User user) {
        return userRepository.findByLogin(user.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserUpdateDTO updateExistingUser(User user, UserUpdateDTO userUpdateDTO) {
        user.setLogin(userUpdateDTO.login());
        user.setFullName(userUpdateDTO.fullName());
        userRepository.save(user);
        return userUpdateDTO;
    }

    public void removeUser(String id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public Car associateCarWithUser(String userId, String carId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Car car = carService.findCarById(carId).orElseThrow(() -> new UsernameNotFoundException("Car not found"));
        user.getCars().add(car);
        userRepository.save(user);
        return car;
    }

    public void disassociateCarFromUser(String userId, String carId) {
        User user = findUserById(userId);
        removeCarFromUserCars(user, carId);
        userRepository.save(user);
    }

    public List<Car> retrieveUserCars(String userId) {
        User user = findUserById(userId);
        return user.getCars();
    }

    private void removeCarFromUserCars(User user, String carId) {
        user.getCars().removeIf(car -> car.getId().equals(carId));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByLogin(username);
    }

    private User findUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
