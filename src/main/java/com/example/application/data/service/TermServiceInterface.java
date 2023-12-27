package com.example.application.data.service;

import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import com.example.application.data.models.TermSort;

import java.util.List;

public interface TermServiceInterface {
    List<Term> fetchTerms(
            int offset,
            int limit,
            List<TermSort> sortOrders);
        int getTermCount();
        int getOffset();

    List<Term> fetchTerms(
            Project filter,
            int offset,
            int limit,
            List<TermSort> sortOrders);

    List<Term> fetchTerms(
            Project filter,
            int offset,
            int limit,
            List<TermSort> sortOrders,
            String filterText);

        TermSort createSort(
                String propertyName,
                boolean descending);
}
