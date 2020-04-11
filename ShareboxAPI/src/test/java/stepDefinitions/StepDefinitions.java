package stepDefinitions;

import static org.junit.Assert.*;

import java.io.IOException;

import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import resources.Utilities;

public class StepDefinitions extends Utilities {
	static String fileID,Token1,Token2;
	static String curDir = System.getProperty("user.dir");

	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;
	Response respActual;

	//Common method for all test cases
	@Given("overriding SSL Certificate")
	public void overriding_SSL_Certificate() {
		useRelaxedHTTPSValidation();
	}

	//Getting the tokens for user1 and user2
	@Given("user {string} credentials {string} {string} to store token")
	public void user_credentials_to_store_token(String user, String userName, String password) throws IOException, InterruptedException {
	    System.out.println("Generating the Token for - "+user);
		if(user.equalsIgnoreCase("user1")){
	    	Token1=getToken(userName,password);
	    	System.out.println("Token1: "+Token1);
	    } 
	    else if(user.equalsIgnoreCase("user2")) {
	    	Token2=getToken(userName,password);
	    	System.out.println("Token2: "+Token2);
	    }
	    
	   
	}
	
	//Request specification for User1
	@Given("the request specification")
	public void the_request_specification() throws IOException {
		requestSpec = given().spec(requestSpecification(Token1));
	}

	//Request specification for User2 for file Accept/Delete
	@Given("the session token of User2 with fileId and AcceptFileFlag")
	public void the_session_token_of_user2_with_fileid_and_acceptfileflag() throws IOException {
		requestSpec = given().
				baseUri(getGlobalValue("baseUrl")).
				queryParam("token", Token2);	
	}

	//Uploadfile API
	@When("User calls uploadFileAPI with {string} http request")
	public void user_calls_uploadFileAPI_with_http_request(String method) throws IOException{
		String resource = getGlobalValue("uploadFileAPI"); 
		String filePath = curDir + getGlobalValue("uploadFilePath"); 

		if(method.equalsIgnoreCase("GET")) { 
			respActual = requestSpec.
					queryParam("fileId", fileID).
					
					when(). 
					get(resource); 
		}

		if(method.equalsIgnoreCase("POST"))  {
			respActual = requestSpec.
					multiPart("file",filePath).
					multiPart("name",getGlobalValue("filename")).
					multiPart("size",getGlobalValue("filesize")).
					multiPart("hash",gethashValue(filePath)). 

					when(). 
					post(resource);
		}

		if(method.equalsIgnoreCase("PUT"))  {

			respActual = requestSpec.
					multiPart("fileId", fileID).
					multiPart("bytesCompleted", getGlobalValue("filesize")). 

					when(). 
					put(resource);
		}
	}

	//Sharefile API
	@When("User calls shareFileAPI with {string} http request")
	public void user_calls_shareFileAPI_with_http_request(String method) throws IOException{
		String resource = getGlobalValue("shareFileAPI"); 

		if(method.equalsIgnoreCase("GET")) { 
			respActual = requestSpec.
					when(). 
					get(resource); 
		}

		if(method.equalsIgnoreCase("POST"))  {
			respActual = requestSpec.
					multiPart("fileId",fileID).
					multiPart("shareTo",getGlobalValue("user2_ID")).

					when(). 
					post(resource);
		}		

		if(method.equalsIgnoreCase("PUT"))  {
			System.out.println("Scenario: File Accept/Reject by user2");
			respActual = requestSpec.
					multiPart("fileId",fileID).
					multiPart("isAccepted",getGlobalValue("acceptFileFlag")).
					log().all().

					when(). 
					put(resource);
			System.out.println(respActual.asString());
		}

		if(method.equalsIgnoreCase("DELETE")) { 
			respActual = requestSpec.
					multiPart("fileId", fileID).	

					when().
					delete(resource); 
		}
	}

	//Status code verification for the requests
	@Then("API call succeed with status code {int}")
	public void api_call_succeed_with_status_code(int code) {
		assertEquals(code,respActual.getStatusCode());

	}

	//Storing fileId in a static variable
	@And("Parse fileId created and save it in variable")
	public void parse_fileid_created_and_save_it_in_variable() {
		fileID = getJsonPath(respActual, "fileId");

	}

	//Response Assertion for the response received from APIs
	@And("Verify {string} in response body is {string}")
	public void verify_in_response_body_is(String keyValue, String expectedValue) {
		assertEquals(expectedValue,getJsonPath(respActual, keyValue));
	}

	//Assertion of fileId
	@And("Verify {string} in response body")
	public void verify_in_response_body(String keyValue) {
		assertEquals(getJsonPath(respActual, keyValue), fileID);
	}

}