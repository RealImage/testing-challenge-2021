import java.util.Map.Entry;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestMy {

	public static void main(String[] args) {
		String baseURI = "https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com//sharebox//api";
		String sessionID = "7b7ca68d-9396-4766-96c3-750a581fc7e1";
		String resource = "upload";
		String fileID = "db3d9de6-c317-4a12-be32-5471fd5d4578";
		
		RestAssured.baseURI = baseURI;
        RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
        
        //query param
        //httpRequest.queryParam("fileId", fileID);
        httpRequest.queryParam("token", sessionID);
        
        //header
        
        httpRequest.header("Content-Type","multipart/form-data")
        	.header("Accept","application/json");
//        
//        //body
//        
//        
        httpRequest.multiPart("fileId" , fileID);
        
        
        Response res = httpRequest.delete("//" + resource);
        
        System.out.println(res.statusCode());
        System.out.println(res.asString());

	}

}
