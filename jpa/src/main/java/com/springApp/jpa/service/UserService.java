package com.springApp.jpa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springApp.jpa.dto.UserDto;
import com.springApp.jpa.entity.User;
import com.springApp.jpa.exception.EmailSendingException;
import com.springApp.jpa.exception.UserServiceException;
import com.springApp.jpa.repository.UserJpaRepository;
import com.springApp.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserJpaRepository jpaRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    EmailService emailService;

    public Iterable<User> saveAll(List<User> users) {
        return jpaRepository.saveAll(users);
    }

    public Iterable<User> findAllUsers() {
        return jpaRepository.findAll();
    }

    public void deleteUser(Long id) {
        jpaRepository.deleteById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return jpaRepository.findByUsername(username);
    }

    public User addUser(User user) {
        return jpaRepository.save(user);
    }

    public User createUserFromDto(UserDto userDto) {
        User user = mapper.convertValue(userDto, User.class);

        User savedUser = jpaRepository.save(user);
        String subject = "Creating user";
        String body = "Dear " + savedUser.getName() + " Thank you for registration! \n your username is " + savedUser.getUsername();

        try {
            emailService.sendEmail(savedUser.getEmail(), subject, body);
        } catch (EmailSendingException e) {
            System.out.println(e.getMessage());
            throw new UserServiceException("User created succesfully but error on sending email", "ERROR_ON_EMAIL_SENDING");
        }

        return savedUser;
    }
}
