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

public class _US11_DiscountTest {

    Faker faker=new Faker();
    String discountDesc;
    String discountCode;
    String discountId;


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
    public void createDiscount(){


    System.out.println(baseURI);

    Map<String,String> discount=new HashMap<>();

    discountDesc=faker.name().lastName();
    discountCode=faker.number().digits(3);
    discount.put("active","true");
    discount.put("description", discountDesc);
    discount.put("code", discountCode);
    discount.put("priority","5");

    discountId=
            given()
                    .spec(recSpec)
                    .body(discount)
                    .log().body()

                    .when()
                    .post("/school-service/api/discounts")

                    .then()
                    .log().body()
                    .statusCode(201)
                    .extract().path("id")
            ;
    System.out.println("discountID= "+ discountId);

        
}
@Test (dependsOnMethods = "createDiscount")
    public void createDiscountNegative(){
     Map<String,String> discount =new HashMap<>();

    discount.put("active","true");
    discount.put("description", discountDesc);
    discount.put("code", discountCode);
    discount.put("priority","5");


    given()
             .spec(recSpec)
             .body(discount)
             .log().body()

             .when()
             .post("/school-service/api/discounts")

             .then()
             .log().body()
             .statusCode(400)
             .body("message", containsString("already"))

             ;

}
@Test(dependsOnMethods = "createDiscountNegative")
    public void updateDiscount (){

    Map<String,String>discount=new HashMap<>();

    discount.put("id", discountId);
    discountDesc="Teni isim3="+faker.name().lastName();
    discount.put("description", discountDesc);
    discount.put("code", discountCode);
    discount.put("priority","5");
    discount.put("active","true");

        given()
                .spec(recSpec)
                .body(discount)

                .when()
                .put("/school-service/api/discounts")

                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(discountDesc))
                ;

    System.out.println("Discount name "+discountDesc);
}


}
