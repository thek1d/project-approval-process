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


    double newFeatures = adjustProject(ressources, workingTime, numFeatures, appCategory);

    if(newFeatures - numFeatures == 0.0){
      throw new ProcessEngineException("No Update done!");
    }else{
      execution.setVariable("features", newFeatures);
    }
  }

  protected double adjustProject(Double ressources, Double workingTime, Double numFeatures, String appCategory){
    double reduction = 0.0;

    //check input data
    if(ressources == null || workingTime == null || numFeatures == null || appCategory == null){
      throw new NullPointerException("Input is null!");
    }else if(ressources < 1.0 || workingTime < 1.0 || numFeatures < 1.0){
      throw new ProcessEngineException("Wrong initial values!");
    } else if(ressources > 50.0 || workingTime > 160.0){
      throw new ProcessEngineException("Project is not implementable!\n Wrong decision!");
    }

    /*
    adjust numFeatures
    */
    
    if(appCategory.equals("App 1")){
      //adjustment for App 1

      if(ressources > 15.0 && workingTime > 40.0){
        reduction = 3.0;
      } else{
        throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
      }
    } else if(appCategory.equals("App 2")){
      //adjustment for App 2

      if(ressources > 10.0 && workingTime > 40.0){
        reduction = 2.0;
      }else{
        throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
      }
    } else if(appCategory.equals("App 3")){
      //adjustment for App 3

      if(ressources > 20.0 && workingTime > 80.0){
        reduction = 5.0;
      }else{
        throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
      }
    } else if(ressources <= 10.0 && workingTime <= 40.0){
      throw new ProcessEngineException("Project is implementable!\n Wrong decision!");
    } else{
      throw new ProcessEngineException("Wrong App Category");
    }

    if((numFeatures - reduction) < 1){
      throw new ProcessEngineException("New amount of features is invalid!");
    } else{
      return numFeatures - reduction;
    }
    
  }

}