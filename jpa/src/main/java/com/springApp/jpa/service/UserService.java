package com.springApp.jpa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springApp.jpa.dto.UserDto;
import com.springApp.jpa.entity.User;
import com.springApp.jpa.repository.UserJpaRepository;
import com.springApp.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;
    @Autowired
    UserJpaRepository jpaRepository;
    @Autowired
    private ObjectMapper mapper;

    public Iterable<User> saveAll(List<User> users){
        return repository.saveAll(users);
    }

    public Iterable<User> findAllUsers(){
        return repository.findAll();
    }

    public void deleteUser(Long id){
        repository.deleteById(id);
    }

    public Optional<User> findUserByUsername(String username){
        return jpaRepository.findByUsername(username);
    }

    public  User addUser(User user){
        return jpaRepository.save(user);
    }

    public User createUserFromDto(UserDto userDto) {
        //        User u = new User();
//        u.setName(userDto.getName());...
        User user = mapper.convertValue(userDto, User.class);
        return jpaRepository.save(user);
    }

}
