package org.example.filessharingsystemspring.repository;

import org.example.filessharingsystemspring.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByName(String name);
    Users findByNameAndPassword(String name, String password);
}