package com.example.application.data.repository;

import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import org.springframework.data.domain.Pageable;
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
    List<Term> findBySourceTermLikeIgnoreCase(String sourceTermPattern, Pageable pageable);
    List<Term> findByProjectIsLike(Project project, Pageable pageable);

    @Query("SELECT t FROM Term t " +
            "WHERE lower(t.sourceTerm) LIKE lower(concat('%', :searchTerm, '%')) " +
            "AND (:project IS NULL OR t.project = :project)")
    List<Term> findByProjectIsLikeAndSourceTermContaining(@Param("project") Project project,@Param("searchTerm") String searchTerm, Pageable pageable);

}
