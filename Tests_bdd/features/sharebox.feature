Feature: Sharebox file upload

Scenario: File Upload and get
    Given user has a file with <token> and <file> and <size> and <hash> and <name>
    When user uploads file
    Then user retrieves file

    Examples:
    |token|file|size|hash|name|
    |"ba800983-c8cf-4d72-bbd9-5496b27c46d1"|"hello.m4"|"100mb"|"heha"|"hello"|
