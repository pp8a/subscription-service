package com.example.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.subscription.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
