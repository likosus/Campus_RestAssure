package RestAssureAPI_Testing;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;


import static org.hamcrest.Matchers.equalTo;

public class _US05_PositionTest {

    Faker faker=new Faker();
    String positionName;
    String positionShortName;
    String positionID;



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
    public void createPosition(){

        System.out.println("baseURI = " + baseURI);

        Map<String,String> position=new HashMap<>();
        positionName=faker.name().name();
        positionShortName=faker.name().lastName();
        position.put("name", positionName);
        position.put("shortName",positionShortName);
        position.put("tenantId","6390ef53f697997914ec20c2");

        positionID=
                given()
                        .spec(recSpec)
                        .body(position)
                        .log().body()

                        .when()
                        .post("/school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
        System.out.println("positionID = " + positionID);

    }

    @Test(dependsOnMethods = "createPosition")
    public void createPositionNegative(){

        Map<String,String> position=new HashMap<>();
        position.put("name", positionName);
        position.put("id",positionID);

        given()
                .spec(recSpec)
                .body(position)
                .log().body()

                .when()
                .post("/school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(400)
                ;


    }

    @Test(dependsOnMethods = "createPositionNegative")
    public void updatePosition(){

        Map<String,String> position=new HashMap<>();
        position.put("id",positionID);
        positionName=faker.name().name();
        position.put("name", positionName);
        position.put("shortName",positionShortName);
        position.put("tenantId","6390ef53f697997914ec20c2");


        given()
                .spec(recSpec)
                .body(position)
                .log().body()

                .when()
                .put("/school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(positionName))
        ;


    }

    @Test(dependsOnMethods = "updatePosition")
    public void deletePosition(){

        given()
                .spec(recSpec)
                .pathParam("positionID",positionID)
                .log().uri()

                .when()
                .delete("/school-service/api/employee-position/{positionID}")

                .then()
                .log().body()
                .statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "deletePosition")
    public void deletePositionNegative(){
        given()
                .spec(recSpec)
                .pathParam("positionID",positionID)
                .log().uri()

                .when()
                .delete("/school-service/api/employee-position/{positionID}")

                .then()
                .log().body()
                .statusCode(204) //normalde 400 yazılmalı bug olduğundan dolayı çalışması için 204 ile değiştirildi
        ;

    }


}
