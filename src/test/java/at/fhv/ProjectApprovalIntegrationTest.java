package at.fhv;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.Variables;

@SpringBootTest(
    properties = {
        "camunda.bpm.generate-unique-process-engine-name=true",
        "camunda.bpm.generate-unique-process-application-name=true",
        "spring.datasource.generate-unique-name=true",
      }
)
public class ProjectApprovalIntegrationTest {

    @Autowired
    ProcessEngine processEngine;  

    @Autowired
    RuntimeService runtimeService;

    @BeforeEach
    public void setUp() {
        init(this.processEngine);
    }


    /**
     * Run a process including the subprocess to make sure the intregration works
     */
    @Test
    public void make_sure_subprocess_gets_integrated(){
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("Project_Approval", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1"));
        assertThat(process).isNotNull();
        assertThat(process).isStarted();

    }
    /*
    private FileValue createDefaultFileValue() {
        FileValue fileValue = Variables.fileValue("tst.txt").file("somebytes".getBytes()).create();
        return fileValue;
    }
    */
}