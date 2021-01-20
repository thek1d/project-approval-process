package at.fhv;

import org.junit.jupiter.api.Test;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.ProcessEngineException;
import org.junit.jupiter.api.Assertions;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake;

public class ProjectAdjustTest {

    @Test
    public void testNegativeNumFeatures(){
        Assertions.assertThrows(ProcessEngineException.class, () -> {
            ProjectAdjust projectAdjust = new ProjectAdjust();
            projectAdjust.adjustProject(20.0, 45.0, 2.0, "App 1", new DelegateExecutionFake());
        });        
    }

    @Test
    public void testImplClassification(){
        Assertions.assertThrows(ProcessEngineException.class, () -> {
            ProjectAdjust projectAdjust = new ProjectAdjust();
            projectAdjust.adjustProject(9.0, 35.0, 9.0, "App 1", new DelegateExecutionFake());
        });  
    }

    @Test
    public void testAppClassification(){
        Assertions.assertThrows(ProcessEngineException.class, () -> {
            ProjectAdjust projectAdjust = new ProjectAdjust();
            projectAdjust.adjustProject(20.0, 45.0, 10.0, "Foo App", new DelegateExecutionFake());
        });  
    }
}