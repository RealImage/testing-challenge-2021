Feature: Validate APIs for communicating with ShareBox server

@PostFile @Regression @Smoke
Scenario Outline: Verify if file is being added succeffully using PostUploadFile API
Given Upload file payload with "<name>" "<size>" "<hash>"
When User calls "PostUploadFile" with "POST" http request
Then The API call got success with status code 200
And verify fileId created maps to "<name>" using "GetParticularFile"

Examples:
	|name   |size |hash |
	|AS7019 |606  |ha1906|
	
@PutUpdateStatus @Regression
Scenario: Verify PutUpdateStatus API update the upload status of a file
Given PutUpdateStatus paylod
When User calls "PutUpdateStatus" with "PUT" http request
Then The API call got success with status code 200
And "message" in the response body is "Bytes has been updated successfully"

@PostSharingFilesToAnotherUser @Regression
Scenario: Verify PostSharingFilesToAnotherUser API sharing files to another User
Given PostSharingFilesToAnotherUser paylod
When User calls "PostSharingFilesToAnotherUser" with "POST" http request
Then The API call got success with status code 200
And "message" in the response body is "Successfully Shared"

@PutAcceptRejectFiles @Regression
Scenario: Verify PutAcceptRejectFiles API Accept/Reject the files from other person
Given PutAcceptRejectFiles paylod
When User calls "PutAcceptRejectFiles" with "PUT" http request
Then The API call got success with status code 200
And "message" in the response body is "File Accepted"


@GetAllFiles @Regression @Smoke
Scenario: Verify getAllFiles API get list of files for a given token to display in the dashboard
Given GetAllFiles payload
When User calls "GetAllFiles" with "Get" http request
Then The API call got success with status code 200
 
@DeleteGivenFileUser1 @Regression
Scenario: Verify DeleteGivenFile API delete the given file
Given DeleteGivenFile paylod
When User calls "DeleteGivenFile" with "Delete" http request
Then The API call got success with status code 200
And "message" in the response body is "File deleted successfully"