Feature: Test Get All files from Account

  Background:
    # Upload a File - Pre - Requisite to verify Get All Files => Use this detail to Assert
    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And form field name = 'file.csv'
    And form field size = '2'
    And form field hash = ''
    And form field file = 'file.csv'
    When method post
    And status 200
    And def createdFileId = response.fileId

  Scenario: Get All Files From Account - Valid Scenario

    * url baseUrl
    Given path 'files'
    * param token = primarytoken
    When method get
    And status 200
    # Fetch all the statuses of all the files returned by the API
    * def statusList = karate.jsonPath(response,"$[*].status")
    # Verify in the Array of Files which is returned - files are either in Status Pending/Completed
    And match statusList contains any ['Pending','Completed']
    # Perform necessary Assertions - This below step takes every FileObject in the Array and asserts the following entities
    And match each response[*] contains {name:#notnull, createdOn: #notnull, bytesCompleted: #notnull,size:2,fileId:#notnull}

  Scenario: Get All Files From Account where there are no files - Create a new account as pre-requisite for this Test case

    * url baseUrl
    Given path 'files'
    # This token is from a account where there are no files
    * param token = newaccountToken
    When method get
    And status 200
    And match response == []

  Scenario: Get All Files From Account - Invalid Token

    * url baseUrl
    Given path 'files'
    #Invalid Token
    * param token = 'abcd-efgh-ijkl'
    When method get
    And status 401

  Scenario: Get All Files From Account - Without Token

    * url baseUrl
    Given path 'files'
    #Notice that there is no Token
    When method get
    And status 401

