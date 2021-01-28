Feature: Review approve project

    Test the project approve

    Scenario: Set project and approve
        Given Projektantrag_subprocess started with customer "Oracle" and "App 1"
        When approver is set to "BeGu"
        And project is a approved with /true/
        Then subprocess has finished