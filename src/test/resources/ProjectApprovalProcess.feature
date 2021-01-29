Feature: Approve project

    Test the project approve

    Scenario: Set project and approve
        Given Projektantrag_subprocess started with customer "Oracle" and "App 1"
        When approver is set to "BeGu"
        And project is a approved with /true/
        Then Process has finished

    Scenario: Project not implementable and rejected
        Given Project_Approval process is started with customer "Oracle" and appCategory "App 1" and numFeatures 30.0
        When Customer request is forwarded
        And Ressources are estimated with 40.0 and time estimated with 120.0
        And Implementation status is "No"
        And Project is rejected
        Then Process has finished

    Scenario: Project is implementable but doesn´t get approved
        Given Project_Approval process is started with customer "Oracle" and appCategory "App 1" and numFeatures 30.0
        When Customer request is forwarded
        And Ressources are estimated with 40.0 and time estimated with 120.0
        And Implementation status is "Yes"
        And Specification is defined by "Super fancy specification"
        And Project application is created
        And Project approval is /false/
        And Project is rejected
        Then Process has finished