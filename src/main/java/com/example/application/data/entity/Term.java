package com.example.application.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "terms")
public class Term {

    /*
    Preferred term
    Synonym
    Abbreviation
    Part of speech
    Definition
    Context
    Note
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    private Project project;

    @Column(name="source_term")
    private String sourceTerm;
    @Column(name="source_synonym")
    private String sourceSynonym;
    @Column(name="source_abbreviation")
    private String sourceAbbreviation;
    @Column(name="source_part_of_speech")
    private String sourcePartOfSpeech;
    @Column(name="source_definition")
    private String sourceDefinition;
    @Column(name="source_context")
    private String sourceContext;
    @Column(name="source_note")
    private String sourceNote;

    @Column(name="target_term")
    private String targetTerm;
    @Column(name="target_synonym")
    private String targetSynonym;
    @Column(name="target_abbreviation")
    private String targetAbbreviation;
    @Column(name="target_part_of_speech")
    private String targetPartOfSpeech;
    @Column(name="target_definition")
    private String targetDefinition;
    @Column(name="target_context")
    private String targetContext;
    @Column(name="target_note")
    private String targeteNote;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getSourceTerm() {
        return sourceTerm;
    }

    public void setSourceTerm(String sourceTerm) {
        this.sourceTerm = sourceTerm;
    }

    public String getSourceSynonym() {
        return sourceSynonym;
    }

    public void setSourceSynonym(String sourceSynonym) {
        this.sourceSynonym = sourceSynonym;
    }

    public String getSourceAbbreviation() {
        return sourceAbbreviation;
    }

    public void setSourceAbbreviation(String sourceAbbreviation) {
        this.sourceAbbreviation = sourceAbbreviation;
    }

    public String getSourcePartOfSpeech() {
        return sourcePartOfSpeech;
    }

    public void setSourcePartOfSpeech(String sourcePartOfSpeech) {
        this.sourcePartOfSpeech = sourcePartOfSpeech;
    }

    public String getSourceDefinition() {
        return sourceDefinition;
    }

    public void setSourceDefinition(String sourceDefinition) {
        this.sourceDefinition = sourceDefinition;
    }

    public String getSourceContext() {
        return sourceContext;
    }

    public void setSourceContext(String sourceContext) {
        this.sourceContext = sourceContext;
    }

    public String getSourceNote() {
        return sourceNote;
    }

    public void setSourceNote(String sourceNote) {
        this.sourceNote = sourceNote;
    }

    public String getTargetTerm() {
        return targetTerm;
    }

    public void setTargetTerm(String targetTerm) {
        this.targetTerm = targetTerm;
    }

    public String getTargetSynonym() {
        return targetSynonym;
    }

    public void setTargetSynonym(String targetSynonym) {
        this.targetSynonym = targetSynonym;
    }

    public String getTargetAbbreviation() {
        return targetAbbreviation;
    }

    public void setTargetAbbreviation(String targetAbbreviation) {
        this.targetAbbreviation = targetAbbreviation;
    }

    public String getTargetPartOfSpeech() {
        return targetPartOfSpeech;
    }

    public void setTargetPartOfSpeech(String targetPartOfSpeech) {
        this.targetPartOfSpeech = targetPartOfSpeech;
    }

    public String getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(String targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

    public String getTargetContext() {
        return targetContext;
    }

    public void setTargetContext(String targetContext) {
        this.targetContext = targetContext;
    }

    public String getTargeteNote() {
        return targeteNote;
    }

    public void setTargeteNote(String targeteNote) {
        this.targeteNote = targeteNote;
    }
}
