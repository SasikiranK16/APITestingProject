package org.lets.shop.tests;

import io.restassured.*;
import static io.restassured.RestAssured.*;
import org.hamcrest.*;
import static org.hamcrest.MatcherAssert.*;
import org.packs.ApiBase;
import org.testng.annotations.Test;

public class ApiTests extends ApiBase {

	@Test
	public void AuthorizationTokens() throws Exception {

		given().spec(createRequestSpecification("https://rahulshettyacademy.com")).when().post("/api/ecom/auth/login")
				.then().spec(createResponseSpecification());
	}
}
