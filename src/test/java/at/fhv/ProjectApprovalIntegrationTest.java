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
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1")); 
        assertThat(process).isNotNull();
        assertThat(process).isStarted();
    }

    //Run a process and make sure it includes service task
    @Test
    public void testServiceTaskIntegration(){
        ProcessInstance process = this.runtimeService.startProcessInstanceByKey("Project_Approval", 
        withVariables("customer", "Foo", "features", 10.0, "appCategory", "App 1", "ressources", 25.0, "time", 45.0, "impl", "Partly")); 
        assertThat(process).isNotNull();
        assertThat(process).isStarted();
        assertThat(process).isWaitingAt("Activity_0y0lger").task().hasName("Kundenanfrage an zuständige Fachabteilung weiterleiten");
        complete(task());
        assertThat(process).isWaitingAt("Activity_1v6xmrt").task().hasName("Ressourcen- und Zeitaufwand abschätzen");
        complete(task());
        assertThat(process).isWaitingAt("Activity_0h12znt").task().hasName("Realisierungs-grad bestimmen");
        complete(task());

        //if process waits at that task, service task got started and finished successfully
        assertThat(process).isWaitingAt("Activity_0maprbt").task().hasName("Projekt-anpassungen kommunizieren");
    }
    
}