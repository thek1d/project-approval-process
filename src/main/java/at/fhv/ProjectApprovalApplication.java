package at.fhv;

import javax.annotation.PostConstruct;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class ProjectApprovalApplication {

  public static void main(String... args) {
    // Avoid resetting URL stream handler factory
    TomcatURLStreamHandlerFactory.disable();
    SpringApplication.run(ProjectApprovalApplication.class, args);
  }

  @Autowired
  protected ProcessEngine processEngine;

  @PostConstruct
  public void deployInvoice() {
    ClassLoader classLoader = ProjectApprovalApplication.class.getClassLoader();

    if(processEngine.getIdentityService().createUserQuery().list().isEmpty()) {
      processEngine.getRepositoryService()
          .createDeployment()
          .addInputStream("process_project_approval.bpmn", classLoader.getResourceAsStream("process_project_approval.bpmn")) //diagram_1.bpmn
          .deploy();
    }
  }
}

