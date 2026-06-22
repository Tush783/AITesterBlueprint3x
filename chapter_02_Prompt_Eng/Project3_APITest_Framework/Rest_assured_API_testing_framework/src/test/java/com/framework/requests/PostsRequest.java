package com.framework.requests;

import com.framework.constants.Endpoints;
import com.framework.pojo.Post;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostsRequest {

    // GET /posts
    public Response getAllPosts() {
        return given()
                .when()
                .get(Endpoints.POSTS)
                .then()
                .extract().response();
    }

    // GET /posts/{id}
    public Response getPostById(int id) {
        return given()
                .pathParam("id", id)
                .when()
                .get(Endpoints.POST_BY_ID)
                .then()
                .extract().response();
    }

    // POST /posts
    public Response createPost(Post post) {
        return given()
                .body(post)
                .when()
                .post(Endpoints.POSTS)
                .then()
                .extract().response();
    }

    // PUT /posts/{id}
    public Response updatePost(int id, Post post) {
        return given()
                .pathParam("id", id)
                .body(post)
                .when()
                .put(Endpoints.POST_BY_ID)
                .then()
                .extract().response();
    }

    // DELETE /posts/{id}
    public Response deletePost(int id) {
        return given()
                .pathParam("id", id)
                .when()
                .delete(Endpoints.POST_BY_ID)
                .then()
                .extract().response();
    }
}
