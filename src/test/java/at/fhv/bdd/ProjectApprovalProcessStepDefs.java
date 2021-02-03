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
        System.out.println("Stopping a ProjectApprovalProcess scenario");
    }

    @Given("Subprocess CheckProjectProposal started with customer {string} and {string}")
    public void check_proposal_subprocess_started(String customer, String appCategory){
        this.instance = this.runtimeService.startProcessInstanceByKey("Check_Project_Proposal", withVariables("customer", customer, "appCategory", appCategory));
        assertThat(this.instance).isNotNull();
        assertThat(this.instance).isStarted();
    }

    @When("Approver is set to {string}")
    public void approver_gets_assigned(String approver){
        assertThat(task(this.instance)).isAssignedTo(approver);
    }

    @And("Project is approved with {}")
    public void project_is_approved(boolean approved){
        complete(task(this.instance), withVariables("approved", approved));
    }


    @Then("Process has finished")
    public void finish_process(){
        assertThat(this.instance).isEnded();
    }

    @Given("Project_Approval process is started with customer {string} and appCategory {string} and numFeatures {double}")
    public void project_Approval_started(String customer, String appCategory, Double numFeatures){
        this.instance = this.runtimeService.startProcessInstanceByKey("Project_Approval", withVariables("customer", customer, "appCategory", appCategory, "features", numFeatures));
        assertThat(this.instance).isNotNull();
        assertThat(this.instance).isStarted();
    }

    @When("Customer request is forwarded")
    public void customer_request_forwarded(){
        complete(task(this.instance));
    }

    @And("Ressources are estimated with {double} and time estimated with {double}")
    public void estimate_ressources_and_time(Double ressources, Double time){
        complete(task(this.instance), withVariables("ressources", ressources, "time", time));
    }

    @And("Implementation status is {string}")
    public void set_implementation_status(String decision){
        complete(task(this.instance), withVariables("impl", decision));
    }

    @And("Project is rejected")
    public void reject_project(){
        complete(task(this.instance));
    }

    @And("Specification is defined by {string}")
    public void create_specification(String kind){
        complete(task(this.instance), withVariables("kind", kind));
    }

    @And("Project application is created")
    public void create_project_application(){
        complete(task(this.instance));
    }

    @And("Project approval is {}")
    public void dont_approve_project(boolean approved){
        ProcessInstance subinstance = this.runtimeService.startProcessInstanceByKey("Check_Project_Proposal", withVariables("customer", "Oracle", "appCategory", "App 2"));
        assertThat(subinstance).isNotNull();
        assertThat(subinstance).isStarted();
        complete(task(subinstance), withVariables("approved", approved));   
        assertThat(subinstance).isEnded();
    }

    @And("Project adjustments are communicated")
    public void communicate_project_adjustments(){
        //check that adjustments are made
        assertThat(this.instance).hasPassed("Define_Project_Adjustments");
        //complete communication
        complete(task(this.instance));
    }

    @And("Customer decided about adjustments with {}")
    public void get_customer_decision(Boolean custagreed){
        complete(task(this.instance), withVariables("custagreed", custagreed));
    }

    @Then("Process must be forwarded again")
    public void wait_for_forwarding(){
        assertThat(this.instance).isWaitingAt("Forward_Cust_Request").task().hasName("Forward customer request to specific department");
    }
}