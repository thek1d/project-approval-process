# SoftwareLifecycle_2020
Project with BPMN for studies
 
## Development
 
### Environment
 
As the development environment VS Code with the following plugins is the recommende IDE:
 
* redhat.java
* alexkrechik.cucumberautocomplete
 
For the development at least the following components need to be installed to run and develop the example:
 
* Java 11
* Maven
 
For the development of BPMN and DMN diagrams the [Camunda Modeler](https://camunda.com/de/download/modeler/) has to be used.
 
### Run the application
 
To run the application execute the following maven command:
 
```
mvn spring-boot:run
```
 
The application starts on http://localhost:8080
 
The default user name and password is specified in the file src/main/resources/application.yaml