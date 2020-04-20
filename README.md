# Test Execution Notes


#### Test Config File :  
qube.properties

BASE_URL.PROD=https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com
USER1_TOKEN.PROD=1901087c-f623-42af-b7c1-975a576daa61
USER1_EMAIL.PROD=test1@qube.com
USER2_TOKEN.PROD=d31b265d-a246-41aa-8fe4-3244a6a3af56
USER2_EMAIL.PROD=test2@qube.com

Contents of config file should be updated before starting the test.
Environment for which tests should run can be controlled from the testsuite parameter.


#### Test Suite File :   
run-qube-tests.xml

#### Test Execution Steps:
Setup the project in any IDE (I used eclipse)
Right click the run-qube-tests.xml and run as TestNg

#### Reports:
Test reports can be found under test-output folder 

----------------------------------------------------------------------------------

# TestFlows

#### File upload Completion flow:

- User uploads a file
- Get file details of uploaded file
- User updates a file for bytesCompleted
- Get file details after updating
- Delete the file

This flow runs for a .txt file and .jpg file. Can be extended to other file formats.

#### File share tests

Accept flow:
- User 1 uploads a file
- User 1 updates the file for bytesCompleted
- User 1 shares the file to user 2
- User 2 accepts the shared file
- User 2 gets file with sharedFiles true
- User 2 gets file with sharedFiles false

Reject flow:
- User 1 uploads a file
- User 1 updates the file for bytesCompleted
- User 1 shares the file to user 2
- User 2 rejects the shared file
- User 2 gets file with sharedFiles true
- User 2 gets file with sharedFiles false

Pending file flow:
- User 1 uploads a file
- User 1 shares a file (when it is in pending status)

#### Delete File Flows

File owner deletes after sharing:
- User 1 uploads a file
- User 1 updates the file bytes Completed
- User 1 shares the file to user 2
- User 2 accepts the file
- User 1 deletes the file
- User 2 gets file with sharedFiles true

Shared user attempts to delete the file:
- User 1 uploads a file
- User 1 updates the file bytes Completed
- User 1 shares the file to user 2
- User 2 accepts the file
- User 2 attempts to delete the file
- User 2 gets file with sharedFiles true

Attempting to delete another user’s file:
- User 1 uploads a file
- User 1 updates the file bytes Completed
- User 2 attempts to delete user 1 file

Deleting a pending file:
- User 1 uploads a file
- User 1 deletes the file in pending status

#### Error scenarios

Unauthorised scenarios:
- Sending every request with invalid tokens

Bad request scenarios:
- Sending requests with missing required params


=======================================================================================

# Automation Coding Challenge 

In this coding challenge, you need to write test cases and automate the same using the APIs of a Web Application called “ShareBox”.

#### Notes: 
* For scenarios to automate, refer: https://github.com/RealImage/testing-challenge-2019#scenarios-to-automate-api-documentation-httpsqubeshareboxdocsapiaryio
* You can choose any programming language preferable to you. 
* You have to fork the Github repository and raise a pull request to the original repository once you completed the coding part with testcases 

#### Prerequisites
* Signup in ShareBox using web URL: https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox
* Generate Token using the option available in the ShareBox Website
* Save the token for automating the APIs of ShareBox 

#### Scenarios to Automate (API documentation: https://qubesharebox.docs.apiary.io): 
* Get the list of files in your account
* Upload any file into your account 
* Check the uploaded files getting listed in your account 
* Delete any file from your account 
* Create another account (same as Prerequisite)  and share files from one account to another account 
* Approve/Reject the shared file from the receiver account 

   **Note:** Add a necessary validation for all the scenarios.


#### API Sample:
##### Host API URL: https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api
##### Scenario: To get a list of files in your account (use the token which you got from the prerequisite)
##### cURL command: 
```
curl -X GET “https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files?getSharedFiles=test&token=<token_generated>” -H “accept: application/json”
```
