# This feature file contains scenarios related to Accepting shared files by Recipient
Feature: Accept Shared File Scenarios

  Background:
    # Upload a File & Share the File to the recipient - Prerequisite for Accept/Declining shared files
    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And form field name = 'newfile.csv'
    And form field size = '2'
    And form field hash = ''
    And form field file = 'newfile.csv'
    When method post
    And status 200
    And def createdFileId = response.fileId

    # Share the File so that we can perform Accept/Reject from the Recipient end
    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = primarytoken
    * form field fileId = createdFileId
    * form field shareTo = 234
    When method post
    And status 200
    # Validate the message Shown
    And match $.message == 'Successfully Shared'

  Scenario: Accept Shared File

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = firstrecipientToken
    * form field fileId = createdFileId
    * form field isAccepted = 'True'
    When method put
    And status 200
    # Validate the message Shown
    And match $.message == 'File Accepted'

  Scenario: Accept Shared File - Invalid File - File which is shared to a different user

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = firstrecipientToken
    # Use a Invalid File Id and try to accept it
    * form field fileId = '2fe3ce69-77dc-4eb7-a289-2767630d5ca3'
    * form field isAccepted = 'True'
    When method put
    And status 400

  Scenario: Accept Shared File - Invalid Recipient Token

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    # Use a Different token which does not belong to the intended Recipient
    * param token = 'c4sda3ec-1ae5-4002-8785-370d0125f88a'
    * form field fileId = '2fe3ce69-77dc-4eb7-a289-2767630d5ca3'
    * form field isAccepted = 'True'
    When method put
    And status 401

  Scenario: Accept Shared File - No Token

    * url baseUrl
    Given path 'files'
    # No token Present in the Call
    And header content-type = 'multipart/form-data'
    * form field fileId = '2fe3ce69-77dc-4eb7-a289-2767630d5ca3'
    * form field isAccepted = 'True'
    When method put
    And status 401
