package org.lets.shop.tests;

import org.hamcrest.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.*;
import static io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class LetsShop {

	public static RequestSpecBuilder requestSpecBuilder;
	public static ResponseSpecBuilder responseSpecBuilder;
	public static RequestSpecification requestSpecification;
	public static ResponseSpecification responseSpecification;
	public static Properties properties;
	public static FileInputStream fileInputStream;

	@BeforeSuite
	public void launch() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userEmail", getDataProperties().getProperty("userEmail"));
		data.put("userPassword", getDataProperties().getProperty("userPassword"));
//		System.out.println(data.toString());
		setIntilizations();
		requestSpecBuilder = requestSpecBuilder.setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).setBody(data).log(LogDetail.METHOD);
		requestSpecification = requestSpecBuilder.build();
		responseSpecBuilder = responseSpecBuilder.expectContentType(ContentType.JSON).expectStatusCode(200)
				.log(LogDetail.METHOD);
		responseSpecification = responseSpecBuilder.build();
	}

	public static void setIntilizations() {

		requestSpecBuilder = new RequestSpecBuilder();
		responseSpecBuilder = new ResponseSpecBuilder();
		getDataProperties();
	}

	public static Properties getDataProperties() {
		properties = new Properties();
		try {
			fileInputStream = new FileInputStream(
					System.getProperty("user.dir") + "//src//test//java//org//lets//shop//tests//data.properties");
			properties.load(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}

	@Test
	public void getAuthorizationTokens() throws Exception {
		Response res = given().spec(requestSpecification).when().post("/api/ecom/auth/login").then()
				.spec(responseSpecification).extract().response();
		System.out.println("Message: " + res.jsonPath().getString("message"));

	}
}
