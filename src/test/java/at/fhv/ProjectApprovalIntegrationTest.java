package at.fhv;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.task.Task;
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


    @Test
    public void testProcessStart(){
        /*
        Verify that a process instance can start
        */
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("Project_Approval", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1"));
        assertThat(process).isNotNull();
        assertThat(process).isStarted();
    }

    
    @Test
    public void testSubProcessStart(){
        /*
        Verify that a subprocess instance can start
        */
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("Check_Project_Proposal", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1")); 
        assertThat(process).isNotNull();
        assertThat(process).isStarted();
    }

    
    @Test
    public void testServiceTaskIntegration(){
        /*
        Verify that the service task gets integrated into the process flow
        */
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("Project_Approval", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1")); 
        assertThat(process).isNotNull();
        assertThat(process).isStarted();

        assertThat(process).isWaitingAt("Forward_Cust_Request").task().hasName("Forward customer request to specific department");
        complete(task());

        assertThat(process).isWaitingAt("Estimate_Ress_Time").task().hasName("Estimate needed ressources and time");
        complete(task(), withVariables("ressources", 25.0, "time", 45.0));

        assertThat(process).isWaitingAt("Define_Realization").task().hasName("Define degree of realization");
        complete(task(), withVariables("impl", "Partly"));

        assertThat(process).hasPassed("Define_Project_Adjustments");
        assertThat(process).isWaitingAt("Communicate_Project_Adjustments").task().hasName("Communicate project adjustments");
    }
    
}