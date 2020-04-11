Feature: Validation File Upload for User
Description: The purpose of this feature is to test the file upload functionality

Background: Overriding SSL Certificate
	Given overriding SSL Certificate

@Test
Scenario Outline: Prerequisite - Getting the tokens for User1 and User2
Description: Pre-Requisite test case to run once before the suite to get user tokens	
	Given user "<user>" credentials "<userName>" "<password>" to store token
Examples:
		|user    |userName                 |password  |
		|user1   |newfirstuser@gmail.com   |qube@123  |
		|user2   |newseconduser2@gmail.com |qube@123  |
	
@Test
Scenario: Verify - Upload any file into your account (File Upload - POST)
Description: Request attributes - filePath, name, size and hash
	Given the request specification
	When User calls uploadFileAPI with "Post" http request
	Then API call succeed with status code 200
	And Parse fileId created and save it in variable
	
@Test	
Scenario: Verify - Success of File Upload (PUT)
Description: Request attributes - fileId and BytesCompleted
	Given the request specification
	When User calls uploadFileAPI with "Put" http request
	Then API call succeed with status code 200
	And Verify "message" in response body is "Bytes has been updated successfully"

@Test
Scenario: Verify - Check the uploaded file getting listed in your account (Get uploaded file - GET)
Description: Request attributes - fileId
	Given the request specification
	When User calls uploadFileAPI with "Get" http request
	Then API call succeed with status code 200
	And Verify "fileId" in response body
	
@Test
Scenario: Verify - Check the uploaded files getting listed in your account (Get List of files - GET)
Description: No request attributes
	Given the request specification
	When User calls shareFileAPI with "Get" http request
	Then API call succeed with status code 200
		
@Test	
Scenario: Verify - Sharing files from one account(User1) to another account(User2) - (Share File - POST)
Description: Request attributes - fileId and shareToId
	Given the request specification
	When User calls shareFileAPI with "Post" http request
	Then API call succeed with status code 200
	And Verify "message" in response body is "Successfully Shared"

@Test	
Scenario: Verify - Approve/Reject the shared file from the receiver account (Approve File - PUT)	
Description: Request attributes - fileId and AcceptedFlag
	Given the session token of User2 with fileId and AcceptFileFlag
	When User calls shareFileAPI with "Put" http request
	Then API call succeed with status code 200
	And Verify "message" in response body is "File Accepted"

@Test		
Scenario: Verify - Delete file from account	(Delete File - Delete)
Description: Request attributes - fileId
	Given the request specification
	When User calls shareFileAPI with "Delete" http request
	Then API call succeed with status code 200
	And Verify "message" in response body is "File deleted successfully"
