package RestAssureAPI_Testing;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;



public class _US10_GradeLevelTest {

    Faker faker=new Faker();
    String gradeLevelId;
    String gradename;
    String[] schoolId={"6390f3207a3bcb6a7ac977f9"};
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
    public void createGradeLevel(){
        Map<String,Object> gradeLevel=new HashMap<>();

        gradename="mustafa";
        gradeLevel.put("name",gradename);
        gradeLevel.put("schoolId",schoolId);
        gradeLevel.put("order", "2");


gradeLevelId=
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


        System.out.println(gradeLevelId);

    }

}
