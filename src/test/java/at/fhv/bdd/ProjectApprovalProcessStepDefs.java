package at.fhv.bdd;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.Given;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.ProcessEngine;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {
        "camunda.bpm.generate-unique-process-engine-name=true",
        "camunda.bpm.generate-unique-process-application-name=true",
        "spring.datasource.generate-unique-name=true",
      }
)
public class ProjectApprovalProcessStepDefs {

    @Autowired
    ProcessEngine processEngine;  

    @Autowired
    RuntimeService runtimeService;
    ProcessInstance instance;

    @Before
    public void setUp() {
        init(this.processEngine);
    }

    @After
    public void tearDown() {
        System.out.println("Stopping a ReviewInvoiceProcess scenario");
    }

    @Given("Projektantrag_subprocess started with customer {string} and {string}")
    public void Projektantrag_subprocess_started(String customer, String appCategory){
        this.instance = this.runtimeService.startProcessInstanceByKey("ProjektantragPruefen", withVariables("customer", customer, "appCategory", appCategory));
        assertThat(this.instance).isNotNull();
        assertThat(this.instance).isStarted();
    }

    @When("approver is set to {string}")
    public void approver_gets_assigned(String approver){
        assertThat(task(this.instance)).isAssignedTo(approver);
    }

    @And("project is a approved with {}")
    public void project_is_approved(boolean approved){
        complete(task(this.instance), withVariables("approved", approved));
    }


    @Then("subprocess has finished")
    public void approver_is_set(){
        assertThat(this.instance).isEnded();
    }
}