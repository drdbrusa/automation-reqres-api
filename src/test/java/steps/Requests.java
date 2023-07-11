package steps;

import classTest.Usuario;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class Requests extends BaseTest {

    private static final String LIST_USER = "/users";
    private static final String LIST_USER_ID = "/users/{userId}";
    private static final String CREATE_USER = "/users";

    @Test
    public void getUserTest() {
        given().
            header("Content-Type", "application/json").
            accept(ContentType.JSON).
            params("page", (4)).
        when().
            get(LIST_USER).
        then().
            statusCode(HttpStatus.SC_OK).
            body("page", equalTo(4)).
            body("data", is(notNullValue())).
            log().all();
    }

    @Test
    public void createUserTest() {
        Map<String, String> usuario = new HashMap<>();
        usuario.put("name", "eduardo");
        usuario.put("job", "qa");
        usuario.put("email", "drd.brusa93@gmail.com");
        given().
           header("Content-Type", "application/json").
           accept(ContentType.JSON).
           body(usuario).
        when().
           post(CREATE_USER).
        then().
           statusCode(HttpStatus.SC_CREATED).
           body("name", is("eduardo")).
        log().all();
    }

    @Test
    public static void getUserIdTest() {
        Usuario usuario = given().
           pathParam("userId", 4).
           header("Content-Type", "application/json").
           accept(ContentType.JSON).
        when().
           get(LIST_USER_ID).
        then().
           statusCode(HttpStatus.SC_OK).
        log().all().
        extract().
           body().jsonPath().getObject("data", Usuario.class);

        assertThat(usuario.getEmail(), containsString("@reqres.in"));
        assertThat(usuario.getName(), containsString("Eve"));
        assertThat(usuario.getLastName(), containsString("Holt"));
    }
}

