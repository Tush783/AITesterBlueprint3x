package com.framework.tests;

import com.aventstack.extentreports.ExtentTest;
import com.framework.base.BaseTest;
import com.framework.requests.UsersRequest;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UsersTest extends BaseTest {

    private UsersRequest usersRequest;

    @BeforeClass
    public void init() {
        usersRequest = new UsersRequest();
    }

    // -------------------------------------------------------------------------
    // GET /users
    // -------------------------------------------------------------------------

    @Test(description = "GET /users returns 200 and a list of 10 users")
    public void getAllUsers_returns200AndListOf10() {
        ExtentTest test = extent.createTest("GET /users — 200 + 10 items");
        extentTest.set(test);

        Response response = usersRequest.getAllUsers();

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.jsonPath().getList("$").size(), equalTo(10));
    }

    // -------------------------------------------------------------------------
    // GET /users/{id}
    // -------------------------------------------------------------------------

    @Test(description = "GET /users/1 returns 200 with id=1, valid name, username, and email")
    public void getUserById_returns200WithValidFields() {
        ExtentTest test = extent.createTest("GET /users/1 — 200 + field assertions");
        extentTest.set(test);

        Response response = usersRequest.getUserById(1);

        assertThat(response.statusCode(),                      equalTo(200));
        assertThat(response.jsonPath().getInt("id"),           equalTo(1));
        assertThat(response.jsonPath().getString("name"),      not(emptyOrNullString()));
        assertThat(response.jsonPath().getString("username"),  not(emptyOrNullString()));
        assertThat(response.jsonPath().getString("email"),     containsString("@"));
    }

    @Test(description = "GET /users/999999 returns 404 for non-existent user")
    public void getUserById_returns404ForMissingUser() {
        ExtentTest test = extent.createTest("GET /users/999999 — 404");
        extentTest.set(test);

        Response response = usersRequest.getUserById(999999);

        assertThat(response.statusCode(), equalTo(404));
    }
}
