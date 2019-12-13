# Automation Coding Challenge 

In this coding challenge, you need to write test cases and automate the same using the APIs of a Web Application called “ShareBox”.

#### Notes: 
* For scenarios to automate, refer: https://github.com/RealImage/testing-challenge-2019#scenarios-to-automate-api-documentation-httpsqubeshareboxdocsapiaryio
* You can choose any programming language preferable to you. 
* You have to fork the Github repository and raise a pull request to the original repository once you completed the coding part with testcases 

#### Prerequisites
* Signup in ShareBox using web URL: https://ec2-13-232-224-131.ap-south-1.compute.amazonaws.com/sharebox
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
##### Host API URL: https://ec2-13-232-224-131.ap-south-1.compute.amazonaws.com/sharebox/api
##### Scenario: To get a list of files in your account (use the token which you got from the prerequisite)
##### cURL command: 
```
curl -X GET “https://ec2-13-232-224-131.ap-south-1.compute.amazonaws.com/sharebox/api/files?getSharedFiles=test&token=<token_generated>” -H “accept: application/json”
```
