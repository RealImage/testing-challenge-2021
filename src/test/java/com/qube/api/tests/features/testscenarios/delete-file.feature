# This Feature file contains test cases for Delete File Scenario
Feature: Delete File Scenarios

  Background:
    # Upload a File - Pre - Requisite to verify Delete File
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

  Scenario: Verify Delete File

    * url baseUrl
    Given path 'files'
    * param token = primarytoken
    # File Id below is created during the Background Step
    * form field fileId = createdFileId
    When method delete
    And status 200
    # Validate the Message shown
    And match $.message == 'File deleted successfully'

  Scenario: Verify Delete - Invalid File Id which belongs to a different Account

    * url baseUrl
    Given path 'files'
    * param token = primarytoken
    # The Below FileId belongs to different account
    * form field fileId = '60816a66-185a-4d6c-94bf-151c1e0dedfr'
    When method delete
    And status 500

  Scenario: Verify Delete - Without FileId

    * url baseUrl
    Given path 'files'
    # Notice that there is no FileID Present
    * param token = primarytoken
    When method delete
    And status 400

  Scenario: Verify Delete with Valid FileId Using Invalid token

    * url baseUrl
    Given path 'files'
    # File Id below is created during the Background Step
    * form field fileId = createdFileId
    * param token = 'abcd-efgh'
    When method delete
    And status 401

  Scenario: Verify Delete with InValid FileId Using Invalid token

    * url baseUrl
    Given path 'files'
    * form field fileId = '60816a66-1s5a-4d6c-94bf-151c1e0dedfr'
    * param token = 'abcd-efgh'
    When method delete
    And status 401
