# Automation Coding Challenge 

Automation of Test cases using the APIs of a Web Application called “ShareBox”.

# EXECUTION VIDEO LINK:
Click on this below link to view the Video Execution: https://www.loom.com/share/3f2ae936a66e43a9bb26d6eb89156d06

#### Notes: 
* For scenarios to automate, refer: https://github.com/RealImage/testing-challenge-2019#scenarios-to-automate-api-documentation-httpsqubeshareboxdocsapiaryio
* You can choose any programming language preferable to you. 
* You have to fork the Github repository and raise a pull request to the original repository once you completed the coding part with testcases 

#### Prerequisites
* Signup four new users in ShareBox using web URL: https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox
* Generate Token using the option available in the ShareBox Website
* Save the token for automating the APIs of ShareBox -> Edit the primaryToken, FirstRecipientToken and SecondRecipientToken in karate-config.js
* We can reuse the users in DB for primary,firstRecipient,SecondRecipient users but need a fresh token for newaccountToken 
* newAccountToken is used to verify that there are no files present when we call getAllFiles endpoint for that particular account.

#### Scenarios Automated (API documentation: https://qubesharebox.docs.apiary.io): 
* Get the list of files in your account
* Upload any file into your account 
* Check the uploaded files getting listed in your account 
* Delete any file from your account 
* Create another account (same as Prerequisite)  and share files from one account to another account 
* Approve/Reject the shared file from the receiver account 

  
## Setup

* Maven should be installed (As specified in pom.xml)
* Java 8+ Version
* Clone/copy project

## Running tests (Sequential Execution)

* cd to project folder

* run `mvn clean test -Dkarate.env="" -Dcucumber.options=""`
    
* or use `mvn clean install

## Statistics

* Total Number of Test Cases : 36
* Total Time Taken to Run: 37 secs
* Number of Threads: 1

 elapsed:  38.34 | threads:    1 | thread time: 37.51 
 features:     7 | ignored:    0 | efficiency: 0.98
 scenarios:   36 | passed:    36 | failed: 0
 
 ### Cucumber reports can be found in the Target folder under cucumber-html-reports folder
`


