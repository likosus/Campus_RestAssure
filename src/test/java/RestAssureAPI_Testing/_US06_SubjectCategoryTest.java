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

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class _US06_SubjectCategoryTest {

    Faker faker=new Faker();

    String subjectCatName;
    String subjectCatCode;
    String subjectCatID;

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
    public void createSubjectCategory(){
        System.out.println("baseURI = " + baseURI);

        Map<String,String>subject=new HashMap<>();
        subjectCatName=faker.name().name();
        subjectCatCode=faker.number().digits(5);
        subject.put("active","true");
        subject.put("name",subjectCatName);
        subject.put("code",subjectCatCode);

        subjectCatID=
                given()
                        .spec(recSpec)
                        .body(subject)
                        .log().body()

                        .when()
                        .post("/school-service/api/subject-categories")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
        System.out.println("positionID = " + subjectCatID);

    }

    @Test(dependsOnMethods = "createSubjectCategory")
    public void createSubjectCategoryNegative(){
        Map<String,String> subject=new HashMap<>();
        subject.put("name", subjectCatName);
        subject.put("id",subjectCatID);

        given()
                .spec(recSpec)
                .body(subject)
                .log().body()

                .when()
                .post("/school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(400)
        ;


    }

    @Test(dependsOnMethods = "createSubjectCategoryNegative")
    public void updateSubjectCategory(){

        Map<String,String> subject=new HashMap<>();
        subject.put("id",subjectCatID);
        subjectCatName=faker.name().name();
        subject.put("name",subjectCatName);
        subject.put("code",subjectCatCode);
        subject.put("active","true");


        given()
                .spec(recSpec)
                .body(subject)
                .log().body()

                .when()
                .put("/school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(subjectCatName))
        ;


    }

    @Test(dependsOnMethods = "updateSubjectCategory")
    public void deleteSubjectCategory(){

        given()
                .spec(recSpec)
                .pathParam("subjectCatID",subjectCatID)
                .log().uri()

                .when()
                .delete("/school-service/api/subject-categories/{subjectCatID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteSubjectCategory")
    public void deleteSubjectCategoryNegative(){

        given()
                .spec(recSpec)
                .pathParam("subjectCatID",subjectCatID)
                .log().uri()

                .when()
                .delete("/school-service/api/subject-categories/{subjectCatID}")

                .then()
                .log().body()
                .statusCode(400)
        ;


    }
}
