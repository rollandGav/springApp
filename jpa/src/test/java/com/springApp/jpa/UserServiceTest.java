package com.springApp.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springApp.jpa.dto.UserDto;
import com.springApp.jpa.entity.User;
import com.springApp.jpa.exception.EmailSendingException;
import com.springApp.jpa.exception.UserServiceException;
import com.springApp.jpa.repository.UserJpaRepository;
import com.springApp.jpa.service.EmailService;
import com.springApp.jpa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Rolland");
        testUser.setAge(29);
        testUser.setUsername("gavrilita1");
        testUser.setEmail("gavrilita.rolland@gmail.com");

        testUserDto = new UserDto();
        testUserDto.setName("Rolland");
        testUserDto.setUsername("gavrilita1");
        testUserDto.setEmail("gavrilita.rolland@gmail.com");
        testUserDto.setAge(29);
    }

    @Test
    void saveAll_ShouldReturnSavedUsers() {
        List<User> users = Arrays.asList(testUser);
        when(jpaRepository.saveAll(users)).thenReturn(users);

        Iterable<User> savedUsers = userService.saveAll(users);

        assertNotNull(savedUsers);
        verify(jpaRepository).saveAll(users);
    }

    @Test
    void saveAll_ShouldHandleEmptyList() {
        List<User> users = Collections.emptyList();
        when(jpaRepository.saveAll(users)).thenReturn(users);

        Iterable<User> savedUsers = userService.saveAll(users);

        assertNotNull(savedUsers);
        assertFalse(savedUsers.iterator().hasNext());
        verify(jpaRepository).saveAll(users);
    }

    @Test
    void findAllUsers_ShouldReturnAllUsers() {
        List<User> users = Arrays.asList(testUser);
        when(jpaRepository.findAll()).thenReturn(users);

        Iterable<User> foundUsers = userService.findAllUsers();

        assertNotNull(foundUsers);
        verify(jpaRepository).findAll();
    }

    @Test
    void findAllUsers_ShouldReturnEmptyList() {
        when(jpaRepository.findAll()).thenReturn(Collections.emptyList());

        Iterable<User> foundUsers = userService.findAllUsers();

        assertNotNull(foundUsers);
        assertFalse(foundUsers.iterator().hasNext());
        verify(jpaRepository).findAll();
    }

    @Test
    void deleteUser_ShouldDeleteUserById() {
        Long userId = 1L;
        doNothing().when(jpaRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(jpaRepository).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldHandleNonExistentIdGracefully() {
        Long userId = 999L;
        doThrow(new IllegalArgumentException("User not found")).when(jpaRepository).deleteById(userId);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userId));
        verify(jpaRepository).deleteById(userId);
    }

    @Test
    void findUserByUsername_ShouldReturnUser_WhenUserExists() {
        when(jpaRepository.findByUsername("gavrilita1")).thenReturn(Optional.of(testUser));

        Optional<User> found = userService.findUserByUsername("gavrilita1");

        assertTrue(found.isPresent());
        assertEquals("gavrilita1", found.get().getUsername());
        assertEquals("Rolland", found.get().getName());
        verify(jpaRepository).findByUsername("gavrilita1");
    }

    @Test
    void findUserByUsername_ShouldReturnEmpty_WhenUserNotFound() {
        when(jpaRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> notFound = userService.findUserByUsername("unknown");

        assertFalse(notFound.isPresent());
        verify(jpaRepository).findByUsername("unknown");
    }

    @Test
    void addUser_ShouldSaveAndReturnUser() {
        when(jpaRepository.save(testUser)).thenReturn(testUser);

        User saved = userService.addUser(testUser);

        assertNotNull(saved);
        assertEquals("Rolland", saved.getName());
        verify(jpaRepository).save(testUser);
    }

    @Test
    void addUser_ShouldThrowException_WhenRepositoryFails() {
        when(jpaRepository.save(testUser)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> userService.addUser(testUser));
        verify(jpaRepository).save(testUser);
    }

    @Test
    void createUserFromDto_ShouldCreateUserAndSendEmail() throws EmailSendingException {
        when(mapper.convertValue(testUserDto, User.class)).thenReturn(testUser);
        when(jpaRepository.save(testUser)).thenReturn(testUser);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        User saved = userService.createUserFromDto(testUserDto);

        assertNotNull(saved);
        verify(mapper).convertValue(testUserDto, User.class);
        verify(jpaRepository).save(testUser);
        verify(emailService).sendEmail(eq(testUser.getEmail()), anyString(), contains(testUser.getUsername()));
    }

    @Test
    void createUserFromDto_ShouldThrowException_WhenEmailFails() throws EmailSendingException {
        when(mapper.convertValue(testUserDto, User.class)).thenReturn(testUser);
        when(jpaRepository.save(testUser)).thenReturn(testUser);
        doThrow(new EmailSendingException("Failed to send email")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.createUserFromDto(testUserDto));

        assertEquals("User created succesfully but error on sending email", exception.getMessage());
        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

}
