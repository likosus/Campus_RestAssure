package RestAssureAPI_Testing;

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
import static org.hamcrest.Matchers.*;

public class _US01_PositionCategoryTest {


    Faker faker=new Faker();
    String positionTypeName;

String postionCatId;

    RequestSpecification recSpec;

    @BeforeClass
    public void Setup()  {
        baseURI="https://test.mersys.io";

        Map<String,String> userCredential=new HashMap<>();
        userCredential.put("username","turkeyts");
        userCredential.put("password","TechnoStudy123");
        userCredential.put("rememberMe","true");

        Cookies cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("/auth/login")

                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
                ;

        recSpec= new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }
       @Test
       public void createPositionType() {

           System.out.println(baseURI);

           Map<String, String> positionType = new HashMap<>();

           positionTypeName = faker.commerce().color();
           positionType.put("name",positionTypeName);

           postionCatId=
                   given()
                           .spec(recSpec)
                           .body(positionType)
                           .log().body()

                           .when()
                           .post("/school-service/api/position-category")

                           .then()
                           .log().body()
                           .statusCode(201)
                           .extract().path("id" )
                   ;
           System.out.println("Positioncat Id is= "+postionCatId);

       }
       @Test(dependsOnMethods = "createPositionType")
    public void createPositionTypeNegative(){

        Map<String,String>positionType=new HashMap<>();


           positionType.put("name",positionTypeName);

           given()
                   .spec(recSpec)
                   .body(positionType)
                   .log().body()

                   .when()
                   .post("/school-service/api/position-category")

                   .then()
                   .log().body()
                   .statusCode(400)
                   .body("message", containsString("already"))
                   ;

     }

     @Test(dependsOnMethods = "createPositionTypeNegative")
     public void updatePositionType(){

           Map<String, String> positionType = new HashMap<>();

           positionType.put("id", postionCatId);
           positionTypeName = "New positon name="+ faker.commerce().color();

           positionType.put("name", positionTypeName);

           given()
                   .spec(recSpec)
                   .body(positionType)

                   .when()
                   .put("/school-service/api/position-category")

                   .then()
                   .log().body()
                   .statusCode(200)
                   .body("name",equalTo(positionTypeName))
;
           System.out.println(positionTypeName);
       }

       @Test(dependsOnMethods = "updatePositionType")
    public void deletePositionType(){
        given()

                .spec(recSpec)
                .pathParam("postionCatId", postionCatId)
                .log().uri()

                .when()
                .delete("/school-service/api/position-category/{postionCatId}")

                .then()
                .log().body()
                .statusCode(204)
                ;
       }
       @Test(dependsOnMethods = "deletePositionType")
    public void deletePositionTypeNegative(){

        given()
                .spec(recSpec)
                .pathParam("postionCatId", postionCatId)
                .log().uri()

                .when()
                .delete("/school-service/api/position-category/{postionCatId}")


                .then()
                .log().body()
                .body("message",equalTo("PositionCategory not  found"))

                ;

       }

}


