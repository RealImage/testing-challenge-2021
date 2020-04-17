# This Feature File contains the test cases for File upload to a particular account
Feature: File Upload Scenarios

  # Using Scenario Outline to redo the test steps with and Without Hash
  Scenario Outline: Verify Upload of File to Account A <HASHVALUE> hash

    * url baseUrl
    Given path 'upload'
    # This token is fetched from karate-config.js
    * param token = primarytoken
    # Set Header
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    # Set Form field Values
    And form field name = 'file.csv'
    And form field size = '2'
    And form field hash = '<HASH>'
    And form field file = 'file.csv'
    When method post
    And status 200
    # Perform Assertions
    And def fileId = response.fileId
    And match $ contains {secretKey : #notnull, accessKey:#notnull, sessionToken:#notnull,fileId: #notnull}

    Examples:
      | HASHVALUE | HASH     |
      | WITHOUT   |          |
      | WITH      | filehash |

  Scenario: Verify Upload of File to Account A without name

    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    And form field size = '2'
    And form field hash = 'hash'
    And form field file = 'file.csv'
    When method post
    And status 400
    And match $.error == "key not found: name"

  Scenario: Verify Upload of File to Account A without size

    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    And form field name = 'file.csv'
    And form field hash = 'hash'
    And form field file = 'file.csv'
    When method post
    And status 400
    And match $.error == "key not found: size"

  Scenario: Verify Upload of File to Account A without file

    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    And form field name = 'file.csv'
    And form field size = '2'
    And form field hash = 'hash'
    When method post
    And status 200
    And match $ contains {secretKey : #notnull, accessKey:#notnull, sessionToken:#notnull,fileId: #notnull}

  Scenario Outline: Verify Upload of File to Account A with <FIELD> field value set to NULL

    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    And form field name = '<NAME>'
    And form field size = '<SIZE>'
    And form field file = '<FILE>'
    And form field hash = 'filehash'
    When method post
    Then status <STATUSCODE>
    And match $ contains {secretKey : #notnull, accessKey:#notnull, sessionToken:#notnull,fileId: #notnull}

    Examples:
      | FIELD | NAME          | SIZE | FILE          | STATUSCODE |
      | NAME  |               | 2    | newFile.csv   |  200       |
      | FILE  | newFile1.txt  | 2    |               |  200       |

  Scenario: Verify Upload of File to Account A with SIZE field value set to NULL

    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    And form field name = 'newFile2.txt'
    And form field size = ''
    And form field file = 'newFile2.txt'
    And form field hash = 'filehash'
    When method post
    Then status 500

  Scenario: Verify Upload of File to Account A with Invalid Token

    * url baseUrl
    Given path 'upload'
    # Invalid Token
    * param token = 'aabcd-efgh'
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    And form field name = 'newFile2.txt'
    And form field size = ''
    And form field file = 'newFile2.txt'
    And form field hash = 'filehash'
    When method post
    Then status 401

  Scenario: Verify Upload of File to Account A with No Token

    * url baseUrl
    Given path 'upload'
    # Note that there is no Token present in the API Call
    And header content-type = 'multipart/form-data; boundary=BOUNDARY'
    And form field name = 'newFile2.txt'
    And form field size = ''
    And form field file = 'newFile2.txt'
    And form field hash = 'filehash'
    When method post
    Then status 401






