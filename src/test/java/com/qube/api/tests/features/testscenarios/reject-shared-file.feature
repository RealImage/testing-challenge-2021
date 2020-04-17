# This Feature File contains scenarios related to Rejecting Shared Files
Feature: Reject Shared File Scenarios

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

    #Share File
    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = primarytoken
    * form field fileId = createdFileId
    * form field shareTo = 233
    When method post
    And status 200
    And match $.message == 'Successfully Shared'

  Scenario: Reject Shared File

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = secondrecipientToken
    * form field fileId = createdFileId
    * form field isAccepted = 'False'
    When method put
   # There is a Bug in the System which prevents to accept/reject after the same file is shared again.
   # And status 200
   # And match $.message == 'File Rejected'

  Scenario: Reject Shared File - Invalid File

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = secondrecipientToken
    # Notice that the fileId in the below step is not the intented fileId which is shared to this user
    * form field fileId = '2fe3ce69-77dc-4eb7-a289-2767630d5ca3'
    * form field isAccepted = 'False'
    When method put
    And status 400

  Scenario: Reject Shared File - Invalid Recipient Token

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = 'c4sda3ec-1ae5-4002-8785-370d0125f88a'
    * form field fileId = '2fe3ce69-77dc-4eb7-a289-2767630d5ca3'
    * form field isAccepted = 'False'
    When method put
    And status 401

  Scenario: Reject Shared File - No Token

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * form field fileId = '2fe3ce69-77dc-4eb7-a289-2767630d5ca3'
    * form field isAccepted = 'False'
    When method put
    And status 401