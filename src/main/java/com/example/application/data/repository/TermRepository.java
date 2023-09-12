package com.example.application.data.repository;

import com.example.application.data.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    @Query("select t from Term t " +
            "where lower(t.sourceTerm) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(t.targetTerm) like lower(concat('%', :searchTerm, '%'))")
    List<Term> search(@Param("searchTerm") String searchTerm);

    @Query("select t from Term t " +
            "where lower(t.project.projectName) like lower(concat('%', :searchTerm, '%')) ")
    List<Term> searchByProj(@Param("searchTerm") String searchTerm);


}
