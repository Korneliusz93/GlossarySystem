package com.example.application.data.entity;

import javax.persistence.*;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int id;

    //@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //private Set<TermbaseEntry> entries;

    /*public void addEntryToProj(TermbaseEntry tEntry){
        entries.add(tEntry);
    }

    public void removeEntry(TermbaseEntry tEntry){
        this.entries.remove(tEntry);
    }*/

    @Column(name = "project_name")
    private String projectName;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Language sourceLanguage;

    @ManyToOne
    private Language targetLanguage;

    //@ManyToMany(fetch = FetchType.EAGER, mappedBy = "projects", cascade = CascadeType.MERGE)
    //private Set<User> users = new HashSet<>();

    /*public void addUser(User user){
        this.users.add(user);
        user.getProjects().add(this);
    }

    public void removeUser(User user){
        boolean check = this.users.contains(user);
        this.users.remove(user);
        user.getProjects().remove(this);
    }*/

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public Language getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;
    }
}