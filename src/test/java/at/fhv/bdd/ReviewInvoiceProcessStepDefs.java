package at.fhv.bdd;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.java.en.When;
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
public class ReviewInvoiceProcessStepDefs {

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

    @Given("Review invoice subprocess started")
    public void review_invoice_subprocess_started(){
        this.instance = this.runtimeService.startProcessInstanceByKey("ReviewInvoice");
        assertThat(this.instance).isNotNull();
        assertThat(this.instance).isStarted();
    }

    @When("Review gets assigned to {string}")
    public void review_gets_assigned(String username){
        complete(task(this.instance), withVariables("reviewer", username));
    }

    @Then("Reviewer is set to {string}")
    public void reviewer_is_set(String username){
        assertThat(task(this.instance)).isAssignedTo(username);
        complete(task(this.instance));
        assertThat(this.instance).isEnded();
    }
}