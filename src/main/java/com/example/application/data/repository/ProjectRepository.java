package com.example.application.data.repository;

import com.example.application.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query("select p from Project p " +
            "where lower(p.projectName) like lower(concat('%', :searchTerm, '%')) " )
    List<Project> search(@Param("searchTerm") String searchTerm);

}
