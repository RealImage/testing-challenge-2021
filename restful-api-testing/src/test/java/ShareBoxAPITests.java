import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;


public class ShareBoxAPITests {

    int statusCode;
    private static final String token = "ea1014dd-d2ab-4403-ab2d-2988b3bd4f70";
    private static final String token2 = "fb8865b9-5f5c-426d-af49-685a88cc7764";
    private static String baseURL = "https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com";
    private static RequestSpecification httpRequest;
    private static Response response;

    @Test
    void fileUpload() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String hash = RandomStringUtils.randomAlphanumeric(10);

        System.out.println("POST request :" + baseURL+"/sharebox/api/upload");

        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("name", name)
                .multiPart("size", "128")
                .multiPart("hash", hash)
                .multiPart("file", new File("./src/test/resources/test-file.xml"))
                .post(baseURL+"/sharebox/api/upload");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());
        statusCode = response.getStatusCode();
        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(response.contentType(), "text/html; charset=utf-8");
        // Data type validation
        Assert.assertEquals("String", jsonPathEvaluator.get("fileId").getClass().getSimpleName());
        Assert.assertEquals("String", jsonPathEvaluator.get("secretKey").getClass().getSimpleName());
        Assert.assertEquals("String", jsonPathEvaluator.get("accessKey").getClass().getSimpleName());
        Assert.assertEquals("String", jsonPathEvaluator.get("sessionToken").getClass().getSimpleName());
    }

    @Test
    void getSpecificfile() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String hash = RandomStringUtils.randomAlphanumeric(10);

        System.out.println("POST request :" + baseURL+"/sharebox/api/upload");

        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("name", name)
                .multiPart("size", "128")
                .multiPart("hash", hash)
                .multiPart("file", new File("./src/test/resources/test-file.xml"))
                .post(baseURL+"/sharebox/api/upload");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        Assert.assertEquals(200, response.getStatusCode());
        String fileId = response.jsonPath().get("fileId").toString();

        httpRequest = null;
        response = null;

        System.out.println("GET request :" + baseURL+"/sharebox/api/upload");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest
                .queryParam("fileId", fileId)
                .queryParam("token", token)
                .get(baseURL+"/sharebox/api/upload");


        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        statusCode = response.getStatusCode();
        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(response.contentType(), "text/html; charset=utf-8");
        // Data validation
        Assert.assertEquals(fileId, jsonPathEvaluator.get("fileId").toString());
        Assert.assertEquals(name, jsonPathEvaluator.get("name").toString());
        Assert.assertEquals("String", jsonPathEvaluator.get("status").getClass().getSimpleName());
        Assert.assertEquals(hash, jsonPathEvaluator.get("fileHash").toString());
        Assert.assertEquals("128", jsonPathEvaluator.get("size").toString(), "128");
        Assert.assertEquals("Integer",jsonPathEvaluator.get("bytesCompleted").getClass().getSimpleName());
        Assert.assertEquals("String", jsonPathEvaluator.get("createdOn").getClass().getSimpleName());
    }

    @Test
    void updateSpecificfile() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String hash = RandomStringUtils.randomAlphanumeric(10);

        System.out.println("POST request :" + baseURL+"/sharebox/api/upload");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("name", name)
                .multiPart("size", "128")
                .multiPart("hash", hash)
                .multiPart("file", new File("./src/test/resources/test-file.xml"))
                .post(baseURL+"/sharebox/api/upload");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        Assert.assertEquals(200, response.getStatusCode());
        String fileId = response.jsonPath().get("fileId").toString();

        httpRequest = null;
        response = null;

        System.out.println("PUT request :" + baseURL+"/sharebox/api/upload");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("fileId", fileId)
                .multiPart("bytesCompleted", "50")
                .put(baseURL+"/sharebox/api/upload");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        statusCode = response.getStatusCode();
        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(response.contentType(), "text/html; charset=utf-8");
        // Data validation
        Assert.assertEquals("Bytes has been updated successfully", jsonPathEvaluator.get("message").toString());
    }

    @Test
    void shareSpecificfileToUser() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String hash = RandomStringUtils.randomAlphanumeric(10);
        String email = "testaccount0403@gmail.com";

        System.out.println("POST request :" + baseURL+"/sharebox/api/upload");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("name", name)
                .multiPart("size", "128")
                .multiPart("hash", hash)
                .multiPart("file", new File("./src/test/resources/test-file.xml"))
                .post(baseURL+"/sharebox/api/upload");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        Assert.assertEquals(200, response.getStatusCode());
        String fileId = response.jsonPath().get("fileId").toString();

        httpRequest = null;
        response = null;

        System.out.println("POST request :" + baseURL+"/sharebox/api/files");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token2)
                .multiPart("fileId", fileId)
                .multiPart("shareTo", email)
                .post(baseURL+"/sharebox/api/files");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        statusCode = response.getStatusCode();
        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals("text/html; charset=utf-8", response.contentType());
        // Data validation
        Assert.assertEquals("Successfully Shared", jsonPathEvaluator.get("message").toString());
    }

    @Test
    void acceptSharedfileFromUser() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String hash = RandomStringUtils.randomAlphanumeric(10);
        String email = RandomStringUtils.randomAlphabetic(8) + "@sharebox.com";

        System.out.println("POST request :" + baseURL+"/sharebox/api/upload");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("name", name)
                .multiPart("size", "128")
                .multiPart("hash", hash)
                .multiPart("file", new File("./src/test/resources/test-file.xml"))
                .post(baseURL+"/sharebox/api/upload");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        Assert.assertEquals(200, response.getStatusCode());
        String fileId = response.jsonPath().get("fileId").toString();

        httpRequest = null;
        response = null;
        boolean isAccepted = true;

        System.out.println("PUT request :" + baseURL+"/sharebox/api/files");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token2)
                .multiPart("fileId", fileId)
                .multiPart("isAccepted", isAccepted)
                .put(baseURL+"/sharebox/api/files");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        statusCode = response.getStatusCode();
        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals("text/html; charset=utf-8", response.contentType());
        // Data validation
        Assert.assertEquals("Successfully Shared", jsonPathEvaluator.get("message").toString());
    }

    @Test
    void deleteSpecificfile() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String hash = RandomStringUtils.randomAlphanumeric(10);
        String email = RandomStringUtils.randomAlphabetic(8) + "@sharebox.com";

        System.out.println("POST request :" + baseURL+"/sharebox/api/upload");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("name", name)
                .multiPart("size", "128")
                .multiPart("hash", hash)
                .multiPart("file", new File("./src/test/resources/test-file.xml"))
                .post(baseURL+"/sharebox/api/upload");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        Assert.assertEquals(200, response.getStatusCode());
        String fileId = response.jsonPath().get("fileId").toString();

        httpRequest = null;
        response = null;

        System.out.println("DELETE request :" + baseURL+"/sharebox/api/files");
        httpRequest = RestAssured.given().relaxedHTTPSValidation();
        response = httpRequest.contentType("multipart/form-data; boundary=BOUNDARY")
                .accept("application/json")
                .queryParam("token", token)
                .multiPart("fileId", fileId)
                .delete(baseURL+"/sharebox/api/files");

        System.out.println("Response :" + response.asString());
        System.out.println("Status Code :" + response.getStatusCode());

        statusCode = response.getStatusCode();
        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals("text/html; charset=utf-8", response.contentType());
        // Data validation
        Assert.assertEquals("File deleted successfully", jsonPathEvaluator.get("message").toString());
    }
}
