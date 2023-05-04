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

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class _US07_SchoolLocationTest {

    Faker faker=new Faker();
    String schoolId;

    String schoolCap;
    String schoolLocName;
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
   public void createSchoolLocation()  {

        System.out.println("baseURI = " + baseURI);
        Map<String,String> schoolLocation=new HashMap<>();

        schoolLocName=faker.address().streetName()+" "+faker.number().digits(5);
        schoolCap=faker.number().digits(2);

        schoolLocation.put("active", "true");
        schoolLocation.put("capacity", schoolCap);
        schoolLocation.put("name", schoolLocName);
        schoolLocation.put("school", "Arsenal ");
        schoolLocation.put("shortname",faker.number().digit());
        schoolLocation.put("type", "CLASS");


        schoolId=
                given()
                        .spec(recSpec)
                        .body(schoolLocation)
                        .log().body()

                        .when()
                        .post("/school-service/api/location")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;

        System.out.println("schoolLocId= "+schoolId);






    }











}
*/