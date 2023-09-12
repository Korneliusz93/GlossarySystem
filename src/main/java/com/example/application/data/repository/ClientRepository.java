package com.example.application.data.repository;

import com.example.application.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c " +
            "where lower(c.customer) like lower(concat('%', :searchTerm, '%')) ")
    List<Client> search(@Param("searchTerm") String searchTerm);
}
