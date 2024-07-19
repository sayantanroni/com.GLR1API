package globalLogic.com;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.jupiter.api.BeforeAll;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;




public class globalLogicAPITesting {

	//Send /get request and validate response code
	
	@Test
    public void testGetRequest() {
        given()
            .when()
                .get("https://httpbin.org/get")
            .then()
                .statusCode(200)
                .body("url", equalTo("https://httpbin.org/get"));
    }

	//Send /post request with Json body and validate response contains relevant data
	//Post method with /response header URI, should return freeform as per user input
	//user input - GlobalLogic 
	//Verify if key value pair -free form & GlobalLogic | Content-Type & application/json is matching in the response
	
	@Test
    public void testPostRequest() {
		
		String urlPOST="https://httpbin.org/response-headers";
		
		String userInputKey1="freeform";
		String userInputValue1="GlobalLogic";
		
		String userInputKey2="Content-Type";
		String userInputValue2="application/json";
		
		HashMap<String,String> postinfo= new HashMap<String,String>();
		postinfo.put(userInputKey1, userInputValue1);
		
        given().contentType("application/json")
        .body(postinfo)
            .when()
                .post(urlPOST)
            .then()
                .statusCode(201)
                .body(postinfo.get(userInputKey1), equalTo(postinfo.get(userInputValue1)))
                .body(postinfo.get(userInputKey2), equalTo(postinfo.get(userInputValue2)));
    }
	
	//Validate request with delayed response /delay/{delay_time}
	//Verify response time is less than or equal to delay time & vcerify response body for url parameter
	@Test
    public void testDelayedResponse() {
        int delayTime = 10;
        String delayURL="https://httpbin.org/delay/"+String.valueOf(delayTime);
        given()
            .pathParam("delay_time", delayTime)
        .when()
            .get("/delay/{delay_time}")
        .then()
            .statusCode(200)
            .time(Matchers.lessThanOrEqualTo(((long)delayTime))) 
            .body("url", equalTo(delayURL)); 
    }
	
	//Write any negative scenario
	//Negative scenario- endpoint GlobalLogic doesnot exist for httpbin.org hence will throw an 404-Not Found error
	@Test
    public void testInvalidEndpoint() {
        given()
            .when()
                .get("https://httpbin.org/globalLogic")
            .then()
                .statusCode(404);
    }
	
	//Simulate authorized Access
	//Successful authentication with flag=True
    @Test
    public void testBasicAuth() {
    	
    	String myValidusername="username";
    	String myValidPassword="password";
    	boolean flag=true;
    	
        given()
            .auth()
                .basic(myValidusername, myValidPassword)
            .when()
                .get("https://httpbin.org/basic-auth/{username}/{password}")
            .then()
                .statusCode(200)
                .body("authenticated", equalTo(flag))
                .body("user", equalTo(myValidusername));
    }
    
    //Simulate Unauthorized Access
    //Unsuccessfull authentication with flag=false & invalid username ,password
    @Test
    public void testBasicAuthInvalid() {
    	
    	String myInvalidusername="Sayantan";
    	String myInvalidPassword="GlobalLogic";
    	boolean invalidFlag=false;
        given()
            .auth()
                .basic(myInvalidusername, myInvalidPassword)
            .when()
                .get("https://httpbin.org/basic-auth/{username}/{password}",myInvalidusername,myInvalidPassword)
            .then()
                .statusCode(401)
                .body("authenticated", equalTo(invalidFlag));
    }
	
    


	}
	

