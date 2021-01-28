package at.fhv.bdd;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration; 
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.Given;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.ProcessEngine;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {
        "camunda.bpm.generate-unique-process-engine-name=true",
        "camunda.bpm.generate-unique-process-application-name=true",
        "spring.datasource.generate-unique-name=true",
      }
)
@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class InvoiceProcessStepDefs {

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
        System.out.println("Stopping a InvoiceProcess scenario");
    }

    @Given("Invoice received with the amount of {float} € and category {string}")
    public void invoice_received_with_the_amount_of(float amount, String category){
        this.instance = this.runtimeService.startProcessInstanceByKey("invoice", 
            withVariables("amount", amount, "invoiceCategory", category, "invoiceNumber", "1234", "invoiceDocument", createDefaultFileValue()));
        assertThat(this.instance).isNotNull();
        assertThat(this.instance).isStarted();
    }

    @When("A user of the {string} group approves the invoice")
    public void user_of_department_approves_invoice(String department){
        assertThat(task(this.instance)).hasCandidateGroup(department);
        complete(task(this.instance), withVariables("approved", true));
    }

    @And("The banking transfer gets done")
    public void banking_transfer_gets_done(){
        complete(task(this.instance));
    }

    @And("The invoice gets archived")
    public void invoice_gets_archived(){
        assertThat(this.instance).hasPassed("ServiceTask_1");
    }

    @Then("The invoice is processed")
    public void invoice_is_processed(){
        assertThat(this.instance).isEnded();
    }

    private FileValue createDefaultFileValue() {
        FileValue fileValue = Variables.fileValue("tst.txt").file("somebytes".getBytes()).create();
        return fileValue;
    }
}