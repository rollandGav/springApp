package com.springApp.jpa.repository;

import com.springApp.jpa.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long>{
}
