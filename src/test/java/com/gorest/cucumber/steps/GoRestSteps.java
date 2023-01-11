package com.gorest.cucumber.steps;

import com.gorest.gorestinfo.UsersSteps;
import com.gorest.utils.TestUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

public class GoRestSteps {

    static ValidatableResponse response;
    static String name = null;
    static String email = null;
    static int id;

    @Steps
    UsersSteps userSteps;
    @Given("^As a user I create get and update and delete user$")
    public void asAUserICreateGetAndUpdateAndDeleteUser() {
    }

    @When("^I create a new user by providing the information name \"([^\"]*)\" email \"([^\"]*)\" gender \"([^\"]*)\" status \"([^\"]*)\"$")
    public void iCreateANewUserByProvidingTheInformationNameEmailGenderStatus(String _name, String _email, String gender, String status) {
        name = TestUtils.getRandomValue() + _name;
        email = TestUtils.getRandomValue() + _email;
        response = userSteps.createUser(name, email, gender, status).statusCode(201);
        id = response.extract().path("id");
    }

    @Then("^I get user information by id$")
    public void iGetUserInformationById() {
        response.statusCode(201);
        response = userSteps.getUserById(id).statusCode(200);
//        List<Object> value = response.extract().path("id");
//        Assert.assertTrue(value.contains(id));
    }

    @When("^Update user details by providing the information name \"([^\"]*)\" email \"([^\"]*)\" gender \"([^\"]*)\" status \"([^\"]*)\"$")
    public void updateUserDetailsByProvidingTheInformationNameEmailGenderStatus(String _name, String _email, String gender, String status) {
        name = name + "_updated";
        response = userSteps.updateUser(id, name, email, gender, status).statusCode(200);
    }

    @Then("^Verify user is updated$")
    public void verifyUserIsUpdated() {
        response.statusCode(200);
        response = userSteps.getUserById(id);
        HashMap<String, Object> value = response.extract().path("");
        Assert.assertThat(value, hasValue(name));
    }

    @Then("^The user id deleted successfully$")
    public void theUserIdDeletedSuccessfully() {
        userSteps.deleteUser(id).statusCode(204);
        userSteps.getUserById(id).statusCode(404);
    }
}

