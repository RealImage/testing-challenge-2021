package ShareBoxApiTests;

import static io.restassured.RestAssured.*;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;

public class TestCases {

    @BeforeTest
    public void setUp() throws Exception {
        try {
            RestAssured.port = port;
            RestAssured.useRelaxedHTTPSValidation();
            RestAssured.config().getSSLConfig().with().keyStore("classpath:keystore.p12", "password");
        } catch (Exception ex) {
            System.out.println("Error while loading keystore >>>>>>>>>");
            ex.printStackTrace();
        }
    }

    @Test
    public void getListOfFiles_StatusCodeVerification() {
        given().queryParam("token", "0bb6ab24-2a35-4484-ba01-9da24212c5c9").
                when().
                get("https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files").
                then().assertThat().statusCode(200);
    }

    @Test
    public void getListOfFiles() {
        Response response = given().queryParam("token", "0bb6ab24-2a35-4484-ba01-9da24212c5c9").
                when().
                get("https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files");

        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> fileName = jsonPathEvaluator.get("name");
        Assert.assertTrue(fileName.get(0).contains("Amber"));
        Assert.assertTrue(fileName.get(0).contains("Situational Scrum Master"));
    }


    @Test
    public void uploadFile() {
        ValidatableResponse response = given().queryParam("token", "0bb6ab24-2a35-4484-ba01-9da24212c5c9")
                .config(config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .contentType("multipart/form-data; boundary=--MyBoundary")
                .accept(ContentType.JSON)
                .body("{\"name\": \"Comparison\",\"size\": \"40439\",\"hash\": \"A65344B2CB5A88B07BD07D09C1B95C11F2390CC61D2F63D31125AB3D43AC7F6C\",\"size\": \"C:\\Users\\diwakar.devapalan\\AppData\\Local\\Temp\\Temp1_RestAssuredDataDriven (1).zip\\ShareBoxAssignment\\Comparison.pdf\"}")
                .when()
                .post("https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/upload")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void verifyUploadedFiles() {
        Response response = given().queryParam("token", "0bb6ab24-2a35-4484-ba01-9da24212c5c9").
                when().
                get("https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files");

        // Get Response Body
        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> fileName = jsonPathEvaluator.get("name");
        Assert.assertTrue(fileName.contains("Comparison"));
    }

    @Test
    public void deleteFile() {
        Response response = given().queryParam("token", "0bb6ab24-2a35-4484-ba01-9da24212c5c9")
                .config(config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .contentType("multipart/form-data; boundary=--MyBoundary")
                .accept(ContentType.JSON)
                .body("{\"fileId\": \"a77ab2d8-55e3-473e-84bc-8cbb8b890e22\"}")
                .when()
                .delete("https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files");

        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> fileName = jsonPathEvaluator.get("name");
        Assert.assertFalse(fileName.contains("Amber"));
    }

    @Test
    public void shareFile() {
        ValidatableResponse response = given().queryParam("token", "0bb6ab24-2a35-4484-ba01-9da24212c5c9")
                .config(config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .contentType("multipart/form-data; boundary=--MyBoundary")
                .accept(ContentType.JSON)
                .body("{\"fileId\": \"a77ab2d8-55e3-473e-84bc-8cbb8b890e22\",\"shareTo\": \"213\"}")
                .post("http://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void acceptFile() {
        Response response = given().queryParam("token", "0bb6ab24-2a35-4484-ba01-9da24212c5c9")
                .config(config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .contentType("multipart/form-data; boundary=--MyBoundary")
                .accept(ContentType.JSON)
                .body("{\"fileId\": \"a77ab2d8-55e3-473e-84bc-8cbb8b890e22\",\"isAccepted\": \"true\"}")
                .put("http://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/files");

        ResponseBody body = response.getBody();
        String bodyStringValue = body.asString();
        JsonPath jsonPathEvaluator = response.jsonPath();
        String message = jsonPathEvaluator.get("message");
        Assert.assertTrue(message.contains("Updated Successfully"));
    }

}
