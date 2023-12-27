package com.example.application.views;

import com.example.application.data.service.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
    //private final CrmService service;
    private ProjectService projectService;
    private TermService termService;

    public DashboardView(ProjectService projectService, TermService termService,
                         LanguageService languageService, CategoryService categoryService,
                            ClientService clientService) {
        this.projectService = projectService;
        this.termService = termService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getProjectStats(),getTermStats(),getClientsChart());
        add();
    }

    private Component getProjectStats() {
        Span stats = new Span(projectService.countProjects() + " projects");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    private Component getTermStats() {
        Span stats = new Span(termService.countTerms() + " terms");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    private Chart getClientsChart() {
        Chart chart = new Chart(ChartType.COLUMN);
        DataSeries dataSeries = new DataSeries();
        dataSeries.setName("Total term count for projects");
        Configuration configuration = chart.getConfiguration();
        projectService.findAllProjects().forEach(project ->
                dataSeries.add(new DataSeriesItem(project.getProjectName(),
                        termService.findAllTermsByProject(project.getProjectName()).size()))
        );
        configuration.setTitle("Project statistics");
        XAxis xAxis = new XAxis();
        xAxis.setTitle("Projects");

        xAxis.setCategories();
        projectService.findAllProjects().forEach(
                project -> xAxis.addCategory(project.getProjectName())
        );

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Terms");
        configuration.addxAxis(xAxis);
        configuration.addyAxis(yAxis);
        configuration.setSeries(dataSeries);
        return chart;
    }
}