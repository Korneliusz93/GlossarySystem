package com.example.application.data.service;

import com.example.application.data.entity.Language;
import com.example.application.data.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    public List<Language> findAllLanguages(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return languageRepository.findAll();
        } else {
            return languageRepository.search(stringFilter);
        }
    }

    public List<Language> findAllLanguages() {
        return languageRepository.findAll();
    }

    public long countLanguages() {
        return languageRepository.count();
    }

    public void deleteLanguage(Language language) {
        languageRepository.delete(language);
    }

    public void saveLanguage(Language language) {
        if (language == null) {
            System.err.println("Customer is null. Are you sure you have connected your form to the application?");
            return;
        }
        languageRepository.save(language);
    }
}
