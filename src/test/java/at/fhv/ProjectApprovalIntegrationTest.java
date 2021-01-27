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


    /*
     * Run a process and make sure it starts
     */
    @Test
    public void testProcessStart(){
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("Project_Approval", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1"));
        assertThat(process).isNotNull();
        assertThat(process).isStarted();
    }

    /*
     * Run a subprocess and make sure it starts
     */
    @Test
    public void testSubProcessStart(){
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("ProjektantragPruefen", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1")); //variables to be changed
        assertThat(process).isNotNull();
        assertThat(process).isStarted();
    }

    /*
    //Run a process and make sure it ends
    @Test
    public void testProcessEnd(){
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("Project_Approval", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1"));
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        assertThat(process).isNotNull();
        //TaskService taskService = rule.getTaskService();
        Task userTask = processEngine.getTaskService().createTaskQuery().processInstanceId(process.getId()).singleResult();
        assertThat(userTask).isNotNull();
        //processEngine.getTaskService().complete("Activity_0y0lger");
        assertThat(process).isEnded();
        
    }
    */
}