package com.framework.tests;

import com.aventstack.extentreports.ExtentTest;
import com.framework.base.BaseTest;
import com.framework.constants.Endpoints;
import com.framework.pojo.Post;
import com.framework.requests.PostsRequest;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostsTest extends BaseTest {

    private PostsRequest postsRequest;

    @BeforeClass
    public void init() {
        postsRequest = new PostsRequest();
    }

    // -------------------------------------------------------------------------
    // GET /posts
    // -------------------------------------------------------------------------

    @Test(description = "GET /posts returns 200 and a list of 100 posts")
    public void getAllPosts_returns200AndListOf100() {
        ExtentTest test = extent.createTest("GET /posts — 200 + 100 items");
        extentTest.set(test);

        Response response = postsRequest.getAllPosts();

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.jsonPath().getList("$").size(), equalTo(100));
    }

    // -------------------------------------------------------------------------
    // GET /posts/{id}
    // -------------------------------------------------------------------------

    @Test(description = "GET /posts/1 returns 200 with id=1 and non-blank fields")
    public void getPostById_returns200WithCorrectFields() {
        ExtentTest test = extent.createTest("GET /posts/1 — 200 + field assertions");
        extentTest.set(test);

        Response response = postsRequest.getPostById(1);

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.jsonPath().getInt("id"),         equalTo(1));
        assertThat(response.jsonPath().getInt("userId"),     greaterThan(0));
        assertThat(response.jsonPath().getString("title"),   not(emptyOrNullString()));
        assertThat(response.jsonPath().getString("body"),    not(emptyOrNullString()));
    }

    @Test(description = "GET /posts/1 response matches JSON schema")
    public void getPostById_matchesJsonSchema() {
        ExtentTest test = extent.createTest("GET /posts/1 — JSON schema validation");
        extentTest.set(test);

        given()
                .pathParam("id", 1)
                .when()
                .get(Endpoints.POST_BY_ID)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/post_schema.json"));
    }

    @Test(description = "GET /posts/999999 returns 404 for non-existent resource")
    public void getPostById_returns404ForMissingPost() {
        ExtentTest test = extent.createTest("GET /posts/999999 — 404");
        extentTest.set(test);

        Response response = postsRequest.getPostById(999999);

        assertThat(response.statusCode(), equalTo(404));
    }

    // -------------------------------------------------------------------------
    // POST /posts  — DataProvider drives three payloads
    // -------------------------------------------------------------------------

    @Test(dataProvider = "postPayloads",
          description = "POST /posts returns 201 and echoes back the sent fields")
    public void createPost_returns201WithEchoedFields(int userId, String title, String body) {
        ExtentTest test = extent.createTest("POST /posts — userId=" + userId);
        extentTest.set(test);

        Post payload  = new Post(userId, title, body);
        Response resp = postsRequest.createPost(payload);

        assertThat(resp.statusCode(),                 equalTo(201));
        assertThat(resp.jsonPath().getInt("id"),      greaterThan(0));
        assertThat(resp.jsonPath().getInt("userId"),  equalTo(userId));
        assertThat(resp.jsonPath().getString("title"), equalTo(title));
        assertThat(resp.jsonPath().getString("body"),  equalTo(body));
    }

    @DataProvider(name = "postPayloads")
    public Object[][] postPayloads() {
        return new Object[][] {
                { 1, "Automation Post Alpha",   "Body content alpha"   },
                { 2, "Automation Post Beta",    "Body content beta"    },
                { 3, "Automation Post Gamma",   "Body content gamma"   },
        };
    }

    // -------------------------------------------------------------------------
    // PUT /posts/{id}
    // -------------------------------------------------------------------------

    @Test(description = "PUT /posts/1 returns 200 with updated title and body")
    public void updatePost_returns200WithUpdatedFields() {
        ExtentTest test = extent.createTest("PUT /posts/1 — 200 + updated fields");
        extentTest.set(test);

        Post updated = new Post(1, "Updated Title", "Updated body content");
        Response resp = postsRequest.updatePost(1, updated);

        assertThat(resp.statusCode(),                       equalTo(200));
        assertThat(resp.jsonPath().getString("title"), equalTo("Updated Title"));
        assertThat(resp.jsonPath().getString("body"),  equalTo("Updated body content"));
    }

    // -------------------------------------------------------------------------
    // DELETE /posts/{id}
    // -------------------------------------------------------------------------

    @Test(description = "DELETE /posts/1 returns 200")
    public void deletePost_returns200() {
        ExtentTest test = extent.createTest("DELETE /posts/1 — 200");
        extentTest.set(test);

        Response resp = postsRequest.deletePost(1);

        assertThat(resp.statusCode(), equalTo(200));
    }
}
