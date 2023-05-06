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


public class _US10_GradeLevelTest {


    String gradelLevelId;
    String name;
    String sName;
    String[] schoolId = {"6390f3207a3bcb6a7ac977f9"};
    RequestSpecification recSpec;
    Map<String, Object> gradeLevel = new HashMap<>();
    Faker faker = new Faker();

    @BeforeClass
    public void Login() {
        baseURI = "https://test.mersys.io";
        Map<String, String> user = new HashMap<>(); //user=userCredential
        user.put("username", "turkeyts");
        user.put("password", "TechnoStudy123");
        user.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)

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
    public void createGradeLevel() {
        name = faker.name().name();
        sName = faker.name().lastName();
        gradeLevel.put("name", name);
        gradeLevel.put("shortName", sName);
        gradeLevel.put("schoolId", schoolId);
        gradeLevel.put("order", "1");

        gradelLevelId =
                given()
                        .spec(recSpec)
                        .body(gradeLevel)
                        .log().body()

                        .when()
                        .post("/school-service/api/grade-levels")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")

        ;

        System.out.println(gradelLevelId);

    }

    @Test(dependsOnMethods = "createGradeLevel")
    public void createGradeLevelNegative() {

        gradeLevel.put("name", name);
        gradeLevel.put("shortName", sName);
        gradeLevel.put("schoolId", schoolId);
        gradeLevel.put("order", "1");

        given()

                .spec(recSpec)
                .body(gradeLevel)
                .log().body()

                .when()
                .post("/school-service/api/grade-levels")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));

    }

    @Test(dependsOnMethods = "createGradeLevelNegative")
    public void updateGradeLevel() {
        name = faker.name().name();

        gradeLevel.put("id", gradelLevelId);
        gradeLevel.put("name", name);
        gradeLevel.put("shortName", sName);
        gradeLevel.put("schoolId", schoolId);
        gradeLevel.put("order", "1");


        given()
                .spec(recSpec)
                .body(gradeLevel)

                .when()
                .put("/school-service/api/grade-levels")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(name))

        ;
        System.out.println(gradeLevel);
    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradelLevel() {

        given()

                .spec(recSpec)
                .pathParam("gradelLevelId", gradelLevelId)

                .when()
                .delete("/school-service/api/grade-levels/{gradelLevelId}")

                .then()
                .log().body()
                .statusCode(200)


                ;

    }

    @Test(dependsOnMethods = "deleteGradelLevel")
    public void deleteGradelLevelNegative(){

        given()
                .spec(recSpec)
                .pathParam("gradelLevelId", gradelLevelId)
                .log().uri()

                .when()
                .delete("/school-service/api/grade-levels/{gradelLevelId}")

                .then()
                .log().body()
                .body("message", equalTo("Grade Level not found."))

                ;

    }
}



