package com.example.application.views.listTerm;

import com.example.application.data.entity.Term;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.stream.Stream;

public class TermDetailsFormLayout extends FormLayout {
    private final TextField sourceTerm = new TextField("Source Term");
    private final TextField sourceSynonym = new TextField("Source Synonym");
    private final TextField sourceAbbreviation = new TextField("Source Abbreviation");
    private final TextField sourcePartOfSpeech = new TextField("Source Part of speech");
    private final TextField sourceDefinition = new TextField("Source Definition");
    private final TextField sourceContext = new TextField("Source Context");
    private final TextField sourceNote = new TextField("Source Note");

    private final TextField targetTerm = new TextField("Target Term");
    private final TextField targetSynonym = new TextField("Target Synonym");
    private final TextField targetAbbreviation = new TextField("Target Abbreviation");
    private final TextField targetPartOfSpeech = new TextField("Target Part of speech");
    private final TextField targetDefinition = new TextField("Target Definition");
    private final TextField targetContext = new TextField("Target Context");
    private final TextField targeteNote = new TextField("Target Note");

    public TermDetailsFormLayout() {
        Stream.of(sourceTerm, targetTerm, sourceSynonym, targetSynonym, sourceAbbreviation, targetAbbreviation,
                        sourcePartOfSpeech, targetPartOfSpeech, sourceDefinition, targetDefinition, sourceContext,
                        targetContext, sourceNote, targeteNote)
                .forEach(field -> {
            field.setReadOnly(true);
            add(field);
        });

        setResponsiveSteps(new ResponsiveStep("0", 2));
        //setColspan(emailField, 3);
        //setColspan(phoneField, 3);
        //setColspan(streetField, 3);
    }

    public void setTerm(Term term) {
        sourceTerm.setValue(term.getSourceTerm());
        targetTerm.setValue(term.getTargetTerm());

        sourceSynonym.setValue(term.getSourceSynonym());
        targetSynonym.setValue(term.getTargetSynonym());

        sourceAbbreviation.setValue(term.getSourceAbbreviation());
        targetAbbreviation.setValue(term.getTargetAbbreviation());

        sourcePartOfSpeech.setValue(term.getSourcePartOfSpeech());
        targetPartOfSpeech.setValue(term.getTargetPartOfSpeech());

        sourceDefinition.setValue(term.getSourceDefinition());
        targetDefinition.setValue(term.getTargetDefinition());

        sourceContext.setValue(term.getSourceContext());
        targetContext.setValue(term.getTargetContext());

        sourceNote.setValue(term.getSourceNote());
        targeteNote.setValue(term.getTargeteNote());

    }
}
