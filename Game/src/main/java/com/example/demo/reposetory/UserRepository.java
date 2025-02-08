package com.example.demo.reposetory; // Fixed typo in package name

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Find user by email
    Optional<User> findByEmail(String email);

    // ✅ Check if email already exists
    boolean existsByEmail(String email);

    // ✅ Find user with the highest score
    Optional<User> findTopByOrderByHighScoreDesc();

    // ✅ Find all users with a score greater than or equal to a given score
    List<User> findByHighScoreGreaterThanEqual(int score);

    // ✅ Delete user by email
    void deleteByEmail(String email);
}
