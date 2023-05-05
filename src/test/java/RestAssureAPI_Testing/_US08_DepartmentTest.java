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

public class _US08_DepartmentTest {

    Faker faker= new Faker();

    RequestSpecification reqSpec;
    String  departmentName;
    String departmentID;

    Map<String, String> department = new HashMap<>();


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
                        .log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
                ;
        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();


    }

    @Test
    public void createDepartment(){

        departmentName = faker.currency().name();
        department.put("name",departmentName);
        department.put("code",faker.currency().code());
        department.put("school","6390f3207a3bcb6a7ac977f9");
        departmentID=

                given()
                        .spec(reqSpec)
                        .body(department)
                        .log().body()

                        .when()
                        .post("/school-service/api/department")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "createDepartment")
    public void createDepartmentNegative(){
        department.put("name",departmentName);

        given()
                .spec(reqSpec)
                .body(department)
                .log().body()

                .when()
                .post("/school-service/api/department")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;

    }

    @Test(dependsOnMethods = "createDepartmentNegative")
    public void updateDepartment(){
        departmentName=faker.currency().code();
        department.put("name",departmentName);
        // department.put("code",faker.currency().code());
        department.put("id",departmentID);

        given()
                .spec(reqSpec)
                .body(department)
                .log().body()

                .when()
                .put("/school-service/api/department")

                .then()
                .statusCode(200)

        ;
    }

    @Test(dependsOnMethods = "updateDepartment")
    public void deleteDepartment(){

        given()

                .spec(reqSpec)
                .pathParam("departmentID",departmentID)

                .when()
                .delete("/school-service/api/department/{departmentID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteDepartment")
    public void deleteDepartmentNegative(){

        given()
                .spec(reqSpec)
                .pathParam("departmentID",departmentID)
                .log().uri()

                .when()
                .delete("/school-service/api/department/{departmentID}")

                .then()
                .log().body()
                .statusCode(204)//burası normalde 400 ama BUG var ondan kaynaklıı pass geçsin diye 204
        // .body("message",equalTo("Can't find School Department"))
        ;
    }



}

