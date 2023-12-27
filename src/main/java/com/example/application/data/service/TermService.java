package com.example.application.data.service;


import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import com.example.application.data.models.TermSort;
import com.example.application.data.repository.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class TermService implements TermServiceInterface{

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

    @Override
    public List<Term> fetchTerms(int offset, int limit, List<TermSort> sortOrders) {
        // Convert TermSort to Spring Data Sort
        List<Sort.Order> orders = sortOrders.stream()
                .filter(sort -> isNotBlank(sort.getPropertyName()))
                .map(sort -> Sort.Order.by(sort.getPropertyName()).with(
                        sort.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC))
                .collect(Collectors.toList());

        Sort sort = Sort.by(orders);

        // Fetch data using sorting criteria
        return termRepository.findAll(PageRequest.of(offset, limit, sort)).getContent();

    }

    @Override
    public List<Term> fetchTerms(Project filter, int offset, int limit, List<TermSort> sortOrders){
        List<Sort.Order> orders = sortOrders.stream()
                .filter(sort -> isNotBlank(sort.getPropertyName()))
                .map(sort -> Sort.Order.by(sort.getPropertyName()).with(
                        sort.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC))
                .collect(Collectors.toList());

        Sort sort = Sort.by(orders);

        return termRepository
                .findByProjectIsLike(
                        filter,
                        PageRequest.of(offset, limit, sort));
    }

    @Override
    public List<Term> fetchTerms(Project filter, int offset, int limit, List<TermSort> sortOrders, String filterText){
        List<Sort.Order> orders = sortOrders.stream()
                .filter(sort -> isNotBlank(sort.getPropertyName()))
                .map(sort -> Sort.Order.by(sort.getPropertyName()).with(
                        sort.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC))
                .collect(Collectors.toList());

        Sort sort = Sort.by(orders);

        return termRepository
                .findByProjectIsLikeAndSourceTermContaining(
                        filter,
                        filterText,
                        PageRequest.of(offset, limit, sort));
    }

    @Override
    public int getTermCount() {
        return 10;
    }

    @Override
    public int getOffset(){
        return 1;
    }

    @Override
    public TermSort createSort(String propertyName, boolean descending) {
        TermSort termSort = new TermSort();
        termSort.setPropertyName(propertyName);
        termSort.setDescending(descending);
        return termSort;
    }
}
