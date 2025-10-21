package com.unidevs.core_system.repository;

import com.unidevs.core_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByLogin(String login);
}
