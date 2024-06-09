package com.puccampinas.projectarqrestfulapi.services;

import com.puccampinas.projectarqrestfulapi.dtos.user.UserUpdateDTO;
import com.puccampinas.projectarqrestfulapi.domain.user.User;
import com.puccampinas.projectarqrestfulapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarService carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }



    @Test
    public void givenInvalidUsername_whenLoadUserByUsername_thenThrowException() {
        when(userRepository.findByLogin("invaliduser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("invaliduser"));
    }

    @Test
    public void givenValidUserId_whenFindUserById_thenReturnUser() {
        User user = new User();
        user.setId("1");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User result = userService.findUserById("1");

        assertEquals("1", result.getId());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    public void givenInvalidUserId_whenFindUserById_thenThrowException() {
        when(userRepository.findById("invalidId")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserById("invalidId"));
    }

    @Test
    public void givenUser_whenVerifyUserExists_thenReturnUser() {
        User user = new User();
        user.setLogin("testuser");
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        User result = userService.verifyUserExists(user);

        assertEquals("testuser", result.getLogin());
        verify(userRepository, times(1)).findByLogin("testuser");
    }

    @Test
    public void givenNonExistentUser_whenVerifyUserExists_thenThrowException() {
        User user = new User();
        user.setLogin("nonexistentuser");
        when(userRepository.findByLogin("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.verifyUserExists(user));
    }

    @Test
    public void givenUserAndUpdateDTO_whenUpdateExistingUser_thenReturnUpdatedDTO() {
        User user = new User();
        UserUpdateDTO updateDTO = new UserUpdateDTO("newlogin", "New Full Name");

        UserUpdateDTO result = userService.updateExistingUser(user, updateDTO);

        assertEquals("newlogin", user.getLogin());
        assertEquals("New Full Name", user.getFullName());
        assertEquals(updateDTO, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void givenUserId_whenRemoveUser_thenUserIsRemoved() {
        User user = new User();
        user.setId("1");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.removeUser("1");

        verify(userRepository, times(1)).delete(user);
    }



}
