# This Feature File contains scenarios related To File Sharing
Feature: File Share Scenarios

  Background:
    # Upload a File - Pre - Requisite to verify GetFileDetails
    * url baseUrl
    Given path 'upload'
    * param token = primarytoken
    And form field name = 'file.csv'
    And form field size = '2'
    And form field hash = ''
    And form field file = 'file.csv'
    When method post
    And status 200
    #Save the created File to be used in the test cases
    And def createdFileId = response.fileId

  Scenario: Verify Share File using Correct UserId who is the Recipient/secondary user

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    # Using Primary Token
    * param token = primarytoken
    * form field fileId = createdFileId
    * form field shareTo = 233
    When method post
    And status 200
    And match $.message == 'Successfully Shared'

  Scenario: Verify Share File using Invalid parameter for ShareTo

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = primarytoken
    * form field fileId = createdFileId
    # Email Id should not be used in Share To Field - UserID should be used.
    * form field shareTo = 'ganeshr@live.in'
    When method post
    And status 500

  Scenario: Verify Share File using Invalid Token

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * param token = 'abcd-efghijkl'
    * form field fileId = createdFileId
    # Email Id should not be used in Share To Field - UserID should be used.
    * form field shareTo = 233
    When method post
    And status 401

  Scenario: Verify Share File without Token

    * url baseUrl
    Given path 'files'
    And header content-type = 'multipart/form-data'
    * form field fileId = createdFileId
    # Email Id should not be used in Share To Field - UserID should be used.
    * form field shareTo = 233
    When method post
    And status 401

