/*package RestAssureAPI_Testing;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


import static io.restassured.RestAssured.*;

public class _US01_PositionCategoryTest {


    Faker faker=new Faker();
    String positionTypeName;



    RequestSpecification recSpec;
   @BeforeClass
   public void Setup() {

       baseURI = "https://test.mersys.io";


       Map<String, String> userCredential = new HashMap<>();
       userCredential.put("username", "turkeyts");
       userCredential.put("passport", "TechnoStudy123");
       userCredential.put("rememberMe", "true");

       Cookies cookies =
               given()
                       .contentType(ContentType.JSON)
                       .body(userCredential)

                       .when()
                       .post("/auth/login")

                       .then()
                       .statusCode(200)
                       .extract().response().getDetailedCookies();

       recSpec = new RequestSpecBuilder()
               .setContentType(ContentType.JSON)
               .addCookies(cookies)
               .build();
   }
       @Test
       public void createPositionType(){

           System.out.println(baseURI);

           Map<String,String> positionType= new HashMap<>();

          positionTypeName=faker.commerce().department();
          positionTypeSN=





*/









