package RestAssureAPI_Testing;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class _US09_BankAccountTest {

    Faker faker=new Faker();
    String bankAccountName;
    String bankAccountID;

    String bankAccountId;
    String bankAccountİban;
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
    public void createBankAccount(){

        System.out.println(baseURI);

        Map<String, String> bankAccount = new HashMap<>();


        bankAccountName = faker.name().name();
        bankAccountİban = faker.number().digits(10);
        bankAccount.put("id", bankAccountID);
        bankAccount.put("name", bankAccountName);
        bankAccount.put("iban", bankAccountİban);
        bankAccount.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccount.put("currency", "TRY");
        bankAccount.put("deleted", "false");
        bankAccount.put("active", "true");


        bankAccountId=
                given()
                        .spec(recSpec)
                        .body(bankAccount)
                        .log().body()

                        .when()
                        .post("/school-service/api/bank-accounts")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
                ;

        System.out.println("BankAccountID = " + bankAccountId);

    }

    @Test(dependsOnMethods = "createBankAccount")
    public void createBankAccountNegative(){

        Map<String, String> bankAccount = new HashMap<>();

        bankAccount.put("name", bankAccountName);
        bankAccount.put("iban", bankAccountİban);
        bankAccount.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccount.put("currency", "TRY");
        bankAccount.put("deleted", "false");
        bankAccount.put("active", "true");

        given()

                .spec(recSpec)
                .body(bankAccount)
                .log().body()

                .when()
                .post("/school-service/api/bank-accounts")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))

                ;

    }

    @Test(dependsOnMethods = "createBankAccountNegative")
    public void UpdateBankAccount(){

        Map<String, String> bankAccount = new HashMap<>();

        bankAccount.put("id", bankAccountId);
        bankAccountİban="yeni iban = " + faker.number().digits(10);
        bankAccount.put("name", bankAccountName);
        bankAccount.put("iban", bankAccountİban);
        bankAccount.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccount.put("currency", "TRY");
        bankAccount.put("deleted", "false");
        bankAccount.put("active", "true");

        given()
                .spec(recSpec)
                .body(bankAccount)

                .when()
                .put("/school-service/api/bank-accounts")

                .then()
                .log().body()
                .statusCode(200)
                .body("iban", equalTo(bankAccountİban))
                ;

        System.out.println("BankAccountİban = " + bankAccountİban);

    }

    @Test(dependsOnMethods = "UpdateBankAccount")
    public void DeleteBankAccount(){

        given()
                .spec(recSpec)
                .pathParam("bankAccountId", bankAccountId)
                .log().uri()

                .when()
                .delete("/school-service/api/bank-accounts/{bankAccountId}")

                .then()
                .log().body()
                .statusCode(200)
                ;

    }

    @Test(dependsOnMethods = "DeleteBankAccount")
    public void DeleteBankAccountNegative(){

        given()
                .spec(recSpec)
                .pathParam("bankAccountId", bankAccountId)
                .log().uri()

                .when()
                .delete("/school-service/api/bank-accounts/{bankAccountId}")

                .then()
                .log().body()
                .body("message", equalTo("Please, bank account must be exist"))

                ;



    }

}
