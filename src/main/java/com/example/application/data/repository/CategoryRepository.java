package com.example.application.data.repository;

import com.example.application.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c " +
            "where lower(c.categoryName) like lower(concat('%', :searchTerm, '%')) ")
    List<Category> search(@Param("searchTerm") String searchTerm);
}
