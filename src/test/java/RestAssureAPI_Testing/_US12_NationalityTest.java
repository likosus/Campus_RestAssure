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

public class _US12_NationalityTest {
Faker faker=new Faker();
    String nationalityID;

    String nationName;
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
    public void createNationality()  {

        System.out.println("baseURI = " + baseURI);

        Map<String,String> nation=new HashMap<>();
        nationName=faker.nation().nationality();
        nation.put("name", nationName);

        nationalityID=
                given()
                        .spec(recSpec)
                        .body(nation)
                        .log().body()

                        .when()
                        .post("/school-service/api/nationality")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
        System.out.println(nationalityID+"nationalityId");

    }

    @Test(dependsOnMethods = "createNationality")
    public void createNationalityNegative(){

        Map<String,String> nation=new HashMap<>();
        nation.put("name", nationName);

        given()
                .spec(recSpec)
                .body(nation)
                .log().body()

                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))


        ;

    }
    @Test(dependsOnMethods = "createNationalityNegative")
    public void updateNation(){


        Map<String,String>nation=new HashMap<>();

        nation.put("id", nationalityID);

        nationName="Yeni ulus3= "+faker.nation().nationality();
        nation.put("name",nationName);

        given()
                .spec(recSpec)
                .body(nation)

                .when()
                .put("/school-service/api/nationality")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(nationName))

                ;
        System.out.println(nationName);
    }

    @Test(dependsOnMethods = "updateNation")
    public void deleteNation(){
        given()
                .spec(recSpec)
                .pathParam("nationalityID", nationalityID)
                .log().uri()

                .when()
                .delete("/school-service/api/nationality/{nationalityID}")

                .then()
                .log().body()
                .statusCode(200)
                ;

    }
@Test(dependsOnMethods = "deleteNation")
    public void deleteNationNegative(){

        given()
                .spec(recSpec)
                .pathParam("nationalityID", nationalityID)
                .log().uri()
                .when()
                .delete("/school-service/api/nationality/{nationalityID}")

                .then()
                .log().body()
                .body("message", equalTo("Nationality not  found"))
                ;




}

}



















