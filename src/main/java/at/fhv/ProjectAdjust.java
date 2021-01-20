package at.fhv;

import java.util.logging.Logger;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.FileValue;

/**
 * <p>This is an empty service implementation illustrating how to use a plain
 * Java Class as a BPMN 2.0 Service Task delegate.</p>
 */
public class ProjectAdjust implements JavaDelegate {

  private final Logger LOGGER = Logger.getLogger(ProjectAdjust.class.getName());

  public void execute(DelegateExecution execution) throws Exception {

    Double ressources = (Double) execution.getVariable("ressources");
    Double workingTime = (Double) execution.getVariable("time");
    String appCategory = (String) execution.getVariable("appCategory");
    Double numFeatures = (Double) execution.getVariable("features");

    adjustProject(ressources, workingTime, numFeatures, appCategory, execution);
  }

  public void adjustProject(Double ressources, Double workingTime, Double numFeatures, String appCategory, DelegateExecution execution){
    if(appCategory.equals("App 1")){
      if(ressources > 15.0 && workingTime > 40.0){
        numFeatures -= 3;
        execution.setVariable("numFeatures", numFeatures);
      } else{
        throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
      }
    } else if(appCategory.equals("App 2")){
      if(ressources > 10.0 && workingTime > 40.0){
        numFeatures -= 5;
        execution.setVariable("numFeatures", numFeatures);
      }
    } else if(appCategory.equals("App 3")){
      if(ressources > 20.0 && workingTime > 80.0){
        numFeatures -= 2;
        execution.setVariable("numFeatures", numFeatures);
      }
    } else if(ressources <= 10.0 && workingTime <= 40.0){
      throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
    } else{
      throw new ProcessEngineException("Wrong App Category");
    }
    
    /*
      if(shouldFail != null && shouldFail) {
        throw new ProcessEngineException("Could not archive invoice...");
      } else {
        LOGGER.info("\n\n  ... Now archiving invoice "+ invoiceNumber
            +", filename: "+invoice.getFilename()+" \n\n");
      }
    */
  }

}