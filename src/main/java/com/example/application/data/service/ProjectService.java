package com.example.application.data.service;
import com.example.application.data.entity.Project;
import com.example.application.data.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> findAllProjects(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return projectRepository.findAll();
        } else {
            return projectRepository.search(stringFilter);
        }
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public long countProjects() {
        return projectRepository.count();
    }

    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    public void saveProject(Project project) {
        if (project == null) {
            System.err.println("Customer is null. Are you sure you have connected your form to the application?");
            return;
        }
        projectRepository.save(project);
    }

    public long getProjectCategoryCount(){
        return projectRepository.count();
    }

}
