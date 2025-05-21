package com.springApp.jpa.service;

import com.springApp.jpa.entity.User;
import com.springApp.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public Iterable<User> saveAll(List<User> users){
        return repository.saveAll(users);
    }

    public Iterable<User> findAllUsers(){
        return repository.findAll();
    }

    public void deleteUser(Long id){
        repository.deleteById(id);
    }
}
