package at.fhv;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * <p>This is an empty service implementation illustrating how to use a plain
 * Java Class as a BPMN 2.0 Service Task delegate.</p>
 */
public class ProjectAdjust implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {

    Double ressources = (Double) execution.getVariable("ressources");
    Double workingTime = (Double) execution.getVariable("time");
    String appCategory = (String) execution.getVariable("appCategory");
    Double numFeatures = (Double) execution.getVariable("features");

    adjustProject(ressources, workingTime, numFeatures, appCategory, execution);
  }

  public void adjustProject(Double ressources, Double workingTime, Double numFeatures, String appCategory, DelegateExecution execution){
    double reduction = 0.0;
    if(appCategory.equals("App 1")){
      if(ressources > 15.0 && workingTime > 40.0){
        reduction = 3.0;
      } else{
        throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
      }
    } else if(appCategory.equals("App 2")){
      if(ressources > 10.0 && workingTime > 40.0){
        reduction = 5.0;
      }
    } else if(appCategory.equals("App 3")){
      if(ressources > 20.0 && workingTime > 80.0){
        reduction = 2.0;
      }
    } else if(ressources <= 10.0 && workingTime <= 40.0){
      throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
    } else{
      throw new ProcessEngineException("Wrong App Category");
    }

    if((numFeatures - reduction) < 0){
      throw new ProcessEngineException("New amount of features is negative!");
    } else{
      numFeatures -= reduction;
      execution.setVariable("numFeatures", numFeatures);
    }
    
  }

}