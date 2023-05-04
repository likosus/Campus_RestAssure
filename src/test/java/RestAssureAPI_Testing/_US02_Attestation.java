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

public class _US02_Attestation {

    Faker faker= new Faker();

    RequestSpecification reqSpec;
    String  attestationName;
    String attestationID;

    Map<String, String> attestation = new HashMap<>();

    @BeforeClass
    public void login(){
        baseURI="https://test.mersys.io";

        Map<String,String> kullnciBilglri=new HashMap<>();
        kullnciBilglri.put("username", "turkeyts");
        kullnciBilglri.put("password", "TechnoStudy123");
        kullnciBilglri.put("rememberMe","true");

        Cookies cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(kullnciBilglri)

                        .when()
                        .post("/auth/login")

                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
                ;
        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();

    }

    @Test
    public void createAttestation(){
        attestationName=faker.file().fileName();
        attestation.put("name",attestationName);
        attestationID=
                given()
                        .spec(reqSpec)
                        .body(attestation)
                        .log().body()


                        .when()
                        .post("/school-service/api/attestation")


                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }


    @Test(dependsOnMethods = "createAttestation")
    public void createAttestationNegative(){

        attestation.put("name",attestationName);

        given()
                .spec(reqSpec)
                .body(attestation)
                .log().body()


                .when()
                .post("/school-service/api/attestation")


                .then()
                // .log().body()
                .statusCode(400)
                .body("message",containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createAttestationNegative")
    public void updateAttestation(){
        attestationName=faker.file().fileName();
        attestation.put("name",attestationName);
        attestation.put("id",attestationID);

        given()
                .spec(reqSpec)
                .body(attestation)
                .log().body()

                .when()
                .put("/school-service/api/attestation")


                .then()
                .statusCode(200)
                .body("name",equalTo(attestationName))
        ;

    }

    @Test(dependsOnMethods = "updateAttestation")
    public void deleteAttestation(){

        given()
                .spec(reqSpec)
                .pathParam("attestationID",attestationID)

                .when()
                .delete("/school-service/api/attestation/{attestationID}")

                .then()
                // .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteAttestation")
    public void deleteAttestationNegative(){

        given()
                .spec(reqSpec)
                .pathParam("attestationID",attestationID)
                .log().uri()

                .when()
                .delete("/school-service/api/attestation/{attestationID}")

                .then()
                //  .log().all()
                .statusCode(400)
                .body("message",equalTo("attestation not found"))
        ;
    }


}