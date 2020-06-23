package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.Util;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class StepDefinition extends Util {
	
	RequestSpecification reqRespo;
	ResponseSpecification res;
	Response response;
	APIResources resourceAPI;
	public static String fileId;
	public static String bytesCompletedSize;
	
	@Given("Upload file payload with {string} {string} {string}")
	public void upload_file_payload_with(String name, String size, String hash) throws Exception {
		
		reqRespo =given().relaxedHTTPSValidation().spec(requestSpecificationUserAN()).
				formParam("name", name).formParam("size", size).formParam("hash", hash).
				multiPart("file", new File(System.getProperty("user.dir")+"/Tulips.jpg"));
	}

	@When("User calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String method) {
		resourceAPI = APIResources.valueOf(resource);
		//comment
		System.out.println(resourceAPI.getResource());
		
		if(method.equalsIgnoreCase("POST"))
		{
			response=reqRespo.when().
			post(resourceAPI.getResource());
		}
		else if(method.equalsIgnoreCase("PUT"))
		{
			response=reqRespo.when().
			put(resourceAPI.getResource());
		}
		else if(method.equalsIgnoreCase("GET"))
		{
			response=reqRespo.when().
			get(resourceAPI.getResource());
		}
		else if(method.equalsIgnoreCase("DELETE"))
		{
			response=reqRespo.when().
			delete(resourceAPI.getResource());
		}
	}
	
	@Then("The API call got success with status code {int}")
	public void the_API_call_got_success_with_status_code(Integer int1) {
		reqRespo.then().spec(responseSpecification());
		assertEquals (response.getStatusCode(),200);
	}

	@Then("verify fileId created maps to {string} using {string}")
	public void verify_fileId_created_maps_to_using(String expectedName, String resource) throws Exception {
         fileId = getJsonPath(response, "fileId");
		reqRespo= given().relaxedHTTPSValidation().spec(requestSpecificationUserAN()).
				  queryParam("fileId", fileId);	
		user_calls_with_http_request(resource, "GET");
		bytesCompletedSize = getJsonPath(response, "size");
		String actualName = getJsonPath(response, "name");
		assertEquals(actualName,expectedName); 
        
	}
	
	@Given("PutUpdateStatus paylod")
	public void putupdatestatus_paylod() throws Exception {
		reqRespo= given().relaxedHTTPSValidation().spec(requestSpecificationUserAN()).
				formParam("fileId", fileId).
				formParam("bytesCompleted", bytesCompletedSize);
	}

	@Then("{string} in the response body is {string}")
	public void in_the_response_body_is(String resource, String expectedName) {
		String actualName = getJsonPath(response, "message");
		System.out.println(actualName);
		assertEquals(actualName,expectedName);
	}
	
	@Given("PostSharingFilesToAnotherUser paylod")
	public void postsharingfilestoanotheruser_paylod() throws Exception {
		reqRespo= given().relaxedHTTPSValidation().spec(requestSpecificationUserAN()).
				formParam("fileId", fileId).
				formParam("shareTo", getGlobalValue("ANuserID"));
	}
	
	@Given("PutAcceptRejectFiles paylod")
	public void putacceptrejectfiles_paylod() throws Exception {
		reqRespo= given().relaxedHTTPSValidation().spec(requestSpecificationUserAS()).
				formParam("fileId", fileId).
				formParam("isAccepted", true);
	}
	
	@Given("GetAllFiles payload")
	public void getallfiles_payload() throws Exception {
		reqRespo= given().relaxedHTTPSValidation().spec(requestSpecificationUserAS());
	}

	@Given("DeleteGivenFile paylod")
	public void deletegivenfile_paylod() throws Exception {
		reqRespo= given().relaxedHTTPSValidation().spec(requestSpecificationUserAS()).
				formParam("fileId", fileId);
	}
}
