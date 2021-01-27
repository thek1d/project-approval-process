package at.fhv;

import org.junit.jupiter.api.Test;
import java.lang.NullPointerException;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.ProcessEngineException;
import org.junit.jupiter.api.Assertions;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ProjectAdjustTest {

    private double ressources;
    private double time;
    private double features;
    private String appCategory;
    private double initialNumFeatures;

    @Test
    public void testCorrectAdjustments(){
        this.ressources = 20.0;
        this.time = 45.0;
        this.features = 6.0;
        this.appCategory = "App 1";
        ProjectAdjust projectAdjust = new ProjectAdjust();
        Assertions.assertEquals(3.0, projectAdjust.adjustProject(this.ressources, this.time, this.features, this.appCategory));
        Assertions.assertEquals(4.0, projectAdjust.adjustProject(this.ressources, this.time, this.features, "App 2"));
        Assertions.assertEquals(1.0, projectAdjust.adjustProject(25.0, 90.0, this.features, "App 3"));
    }

    @Test
    public void testNegativeNumFeatures(){
        this.ressources = 20.0;
        this.time = 45.0;
        this.features = 2.0;
        this.appCategory = "App 1";
        Assertions.assertThrows(ProcessEngineException.class, () -> {
            ProjectAdjust projectAdjust = new ProjectAdjust();
            projectAdjust.adjustProject(this.ressources, this.time, this.features, this.appCategory);
        });        
    }

    @Test
    public void testWrongInitialValues(){
        this.ressources = -2.0;
        this.time = -2.0;
        this.features = 0.0;
        this.appCategory = "Foo App";
        Assertions.assertThrows(ProcessEngineException.class, () -> {
            ProjectAdjust projectAdjust = new ProjectAdjust();
            projectAdjust.adjustProject(this.ressources, this.time, this.features, this.appCategory);
        });    
    }

    @Test
    public void testCompleteness(){
        this.ressources = 10.0;
        this.time = 35.0;
        this.features = 10.0;
        this.appCategory = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            ProjectAdjust projectAdjust = new ProjectAdjust();
            projectAdjust.adjustProject(this.ressources, this.time, this.features, this.appCategory);
        });    
    }

    @Test
    public void testImplClassification(){
        this.ressources = 9.0;
        this.time = 35.0;
        this.features = 9.0;
        this.appCategory = "App 1";
        Assertions.assertThrows(ProcessEngineException.class, () -> {
            ProjectAdjust projectAdjust = new ProjectAdjust();
            projectAdjust.adjustProject(this.ressources, this.time, this.features, this.appCategory);
        });  
    }
}