package com.example.application.data.repository;

import com.example.application.data.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("select l from Language l " +
            "where lower(l.languageCode) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(l.languageName) like lower(concat('%', :searchTerm, '%'))")
    List<Language> search(@Param("searchTerm") String searchTerm);
}
