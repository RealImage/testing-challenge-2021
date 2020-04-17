# This Feature File contains scenarios for Get Uploaded file Details
Feature: Get Upload Details Scenarios

  Background:
    # Upload a File - Pre - Requisite to verify Get File Details - baseurl is set in karate-config.js
    * url baseUrl
    Given path 'upload'
    # use primary Token set in karate-config.js
    * param token = primarytoken
    And form field name = 'file.csv'
    And form field size = '2'
    And form field hash = ''
    And form field file = 'file.csv'
    When method post
    And status 200
    # Save the fileId to be used in the below test cases
    And def createdFileId = response.fileId

  Scenario: Get Upload Details using the same user Token which is used to Upload the File

    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    * param fileId = createdFileId
    When method get
    And status 200
    # Perform Assertions for the file which is created in the Pre-Requisite
    And match $ contains {status:'Completed',name:'file.csv',fileHash:'',createdOn:#notnull, bytesCompleted:2,size:2}
    And match $.fileId == createdFileId

  Scenario: Get Upload Details using a different Valid Token which is not used to Upload the File

    * url baseUrl
    Given path 'upload'
    # The below token is a valid token but it belongs to a different user
    * param token = 'd0e04b66-8eb5-4142-a5ad-08b053ec02a8'
    * param fileId = createdFileId
    When method get
    And status 401

  Scenario: Get Upload Details - Invalid FileId

    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    # Notice that fileId here is Invalid
    * param fileId = 'abcd-efgh-ijkl'
    When method get
    And status 500

    # Verify Upload using NO & INVALID TOKEN
  Scenario Outline: Get Upload Details using <TYPE> Token

    * url baseUrl
    Given path 'upload'
    * param token = '<TOKEN>'
    * param fileId = createdFileId
    When method get
    And status <STATUSCODE>

    Examples:
      | TYPE    | TOKEN | STATUSCODE |
      | NO      |       | 401        |
      | INVALID | abcd  | 401        |