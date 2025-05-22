package com.springApp.jpa.repository;

import com.springApp.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface  UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
//    List<User> findByActive(boolean active);
//    List<User> findAllByOrderByUsernameAsc();
//    List<User> findByActiveOrderByUsernameAsc(Boolean bool);
//    List<User> findByLocalDateBetween(LocalDate start, LocalDate end);
//    List<User> findByUsernameContaining(String text);
//    List<User> findByUsernameStartingWith(String text);

    @Query("SELECT u FROM User u where u.age > 30")
    List<User> findUserOver30();

    @Query(value = "SELECT * FROM users where age > 30", nativeQuery = true)
    List<User> findUsersOver30NativeQuery();
}
