package com.example.application.data.service;


import com.example.application.data.entity.Term;
import com.example.application.data.repository.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TermService {

    @Autowired
    private TermRepository termRepository;

    public List<Term> findAllTerms(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return termRepository.findAll();
        } else {
            return termRepository.search(stringFilter);
        }
    }

    public List<Term> findAllTermsByProject(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return termRepository.findAll();
        } else {
            return termRepository.searchByProj(stringFilter);
        }
    }

    public List<Term> findAllTerms() {
        return termRepository.findAll();
    }

    public long countTerms() {
        return termRepository.count();
    }

    public void deleteTerm(Term term) {
        termRepository.delete(term);
    }

    public void saveTerm(Term term) {
        if (term == null) {
            System.err.println("Customer is null. Are you sure you have connected your form to the application?");
            return;
        }
        termRepository.save(term);
    }

}
