Feature: Approve project

    Test the project approval

    Scenario: Set project and approve
        Given Subprocess CheckProjectProposal started with customer "Oracle" and "App 1"
        When Approver is set to "BeGu"
        And Project is approved with /true/
        Then Process has finished

    Scenario: Project not implementable and rejected
        Given Project_Approval process is started with customer "Oracle" and appCategory "App 1" and numFeatures 30.0
        When Customer request is forwarded
        And Ressources are estimated with 40.0 and time estimated with 120.0
        And Implementation status is "No"
        And Project is rejected
        Then Process has finished

    Scenario: Project is partly implementable and customer declines changes
        Given Project_Approval process is started with customer "Oracle" and appCategory "App 1" and numFeatures 30.0
        When Customer request is forwarded
        And Ressources are estimated with 40.0 and time estimated with 120.0
        And Implementation status is "Partly"
        And Project adjustments are communicated
        And Customer decided about adjustments with /false/
        And Project is rejected
        Then Process has finished