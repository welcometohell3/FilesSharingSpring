package org.example.filessharingsystemspring.repository;

import org.example.filessharingsystemspring.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilesRepository extends JpaRepository<Files, Long> {
    List<Files> findByUsersId(Long userId);

}

