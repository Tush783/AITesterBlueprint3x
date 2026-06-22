package com.framework.requests;

import com.framework.constants.Endpoints;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UsersRequest {

    // GET /users
    public Response getAllUsers() {
        return given()
                .when()
                .get(Endpoints.USERS)
                .then()
                .extract().response();
    }

    // GET /users/{id}
    public Response getUserById(int id) {
        return given()
                .pathParam("id", id)
                .when()
                .get(Endpoints.USER_BY_ID)
                .then()
                .extract().response();
    }
}
