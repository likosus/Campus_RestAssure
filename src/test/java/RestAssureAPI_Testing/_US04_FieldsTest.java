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

public class _US04_FieldsTest {

    Faker faker= new Faker();

    RequestSpecification reqSpec;

    String fieldsName;
    String fieldsID;
    String code;

    String schoolID="6390f3207a3bcb6a7ac977f9";
    Map<String, String> fields = new HashMap<>();

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
                        .log().body()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
                ;
        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();



    }

    @Test
    public void createFields() {
        fieldsName = faker.name().fullName();
        fields.put("name", fieldsName);
        code = faker.code().asin();
        fields.put("code", code);
        fields.put("schoolId",schoolID );
        fields.put("type", "INTEGER");

        fieldsID =
                given()
                        .spec(reqSpec)
                        .contentType(ContentType.JSON)
                        .body(fields)

                        .log().body()
                        .when()
                        .post("school-service/api/entity-field")
                        .then()

                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;


    }

    @Test(dependsOnMethods = "createFields")
    public void createFieldsNegative() {

        fields.put("name", fieldsName);
        fields.put("code", code);

        given()
                .spec(reqSpec)
                .body(fields)
                .log().body()


                .when()
                .post("school-service/api/entity-field")


                .then()
                // .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;

    }


    @Test(dependsOnMethods = "createFieldsNegative")
    public void updateFields() {

        fieldsName = faker.name().fullName();
        fields.put("name", fieldsName);
        code = faker.code().asin();
        fields.put("code", code);
        fields.put("id",fieldsID);


        given()


                .spec(reqSpec)
                .body(fields)
                .log().body()


                .when()
                .put("school-service/api/entity-field")


                .then()
                .statusCode(200)
                .body("name", equalTo(fieldsName))
        ;


    }

    @Test(dependsOnMethods = "updateFields")
    public void deleteFields() {

        given()

                .spec(reqSpec)
                .pathParam("fieldsID", fieldsID)

                .when()
                .delete("school-service/api/entity-field/{fieldsID}")

                .then()
                // .log().body()
                .statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "deleteFields")
    public void deleteFieldsNegative() {

        given()
                .spec(reqSpec)
                .pathParam("fieldsID", fieldsID)

                .when()
                .delete("school-service/api/entity-field/{fieldsID}")

                .then()
                //.log().body()
                .statusCode(400)

        ;
    }


}





