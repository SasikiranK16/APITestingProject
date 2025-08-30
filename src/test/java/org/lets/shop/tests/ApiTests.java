package org.lets.shop.tests;

import io.restassured.*;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import org.hamcrest.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;

import org.packs.ApiBase;
import org.testng.annotations.Test;

public class ApiTests extends ApiBase {

	@Test
	public void AuthorizationTokens() throws Exception {

		given().spec(createRequestSpecification("https://rahulshettyacademy.com")).when().post("/api/ecom/auth/login")
				.then().spec(createResponseSpecification());
		System.out.println("Token:" + getToken());
		System.out.println("Response:" + getResponses().jsonPath().getString("userId"));
	}

	/* Test AddProduct with Updated Code */

	@Test
	public void testName() throws Exception {
		given().baseUri("https://rahulshettyacademy.com").contentType(ContentType.MULTIPART)
				.header("authorization", getToken()).log().all().param("productName", "weeee")
				.param("productAddedBy", getUserId())

				.param("productCategory", "eee").param("productSubCategory", "Hiells").param("productPrice", "2222")
				.param("productDescription", "wwww").param("productFor", "eerrr")
				.multiPart("productImage", new File("pro3.jpg")).when().post("/api/ecom/product/add-product").then()
				.spec(getDetailsProduct());

	}

	@Test
	public void tests() throws Exception {
		given().spec(createAddProduct(properties.getProperty("site"), "welecos", "eeee", "eeeeer", "55555", "eeee",
				"wwwww", "pro3.jpg")).when().post("/api/ecom/product/add-product").then().spec(getDetailsProduct());
	}
}
