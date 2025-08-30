package org.packs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.restassured.*;
import static io.restassured.RestAssured.*;
import org.hamcrest.*;
import static org.hamcrest.MatcherAssert.*;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.internal.multipart.MultiPartSpecificationImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiBase {

	public static Properties properties;
	public static FileInputStream fileInputStream;
	public static RequestSpecBuilder requestSpecBuilder;
	public static RequestSpecification requestSpecification;
	public static ResponseSpecBuilder responseSpecBuilder;
	public static ResponseSpecification responseSpecification;
	public static Response response, response2;
	public static MultiPartSpecBuilder multiPartSpecBuilder;
	public static MultiPartSpecification multiPartSpecification;

	public static Properties getFileDetails(String fileName) {
		properties = new Properties();
		try {
			fileInputStream = new FileInputStream(System.getProperty("user.dir")
					+ "\\src\\test\\java\\org\\lets\\shop\\tests\\" + fileName + ".properties");
			properties.load(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}

	public static RequestSpecification createRequestSpecification(String url) {
		requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.setBaseUri(url).setContentType(ContentType.JSON)
				.setBody(setAuthBody(properties.getProperty("userEmail"), properties.getProperty("userPassword")))
				.log(LogDetail.ALL);
		requestSpecification = requestSpecBuilder.build();
		createResponseSpecification();
		return requestSpecification;
	}

	public static Response getResponses() {
		Response myRes = given().spec(createRequestSpecification(properties.getProperty("site"))).when()
				.post("/api/ecom/auth/login").then().spec(createResponseSpecification()).extract().response();
		return myRes;
	}

	public static String getToken() {
		String tokens = getResponses().jsonPath().getString("token");
		return tokens;
	}

	public static String getUserId() {
		return getResponses().jsonPath().getString("userId");
	}

	public static ResponseSpecification createResponseSpecification() {
		responseSpecBuilder = new ResponseSpecBuilder();
		responseSpecBuilder.expectStatusCode(200).expectContentType(ContentType.JSON).log(LogDetail.ALL);
		responseSpecification = responseSpecBuilder.build();
		return responseSpecification;
	}

	public static HashMap<String, String> setAuthBody(String uname, String passcode) {

		HashMap<String, String> bodyDetails = new HashMap<String, String>();
		bodyDetails.put("userEmail", uname);
		bodyDetails.put("userPassword", passcode);
		return bodyDetails;
	}

	@BeforeSuite
	public void launch() {
		getFileDetails("data");
	}

	public static ResponseSpecification getDetailsProduct() {
		responseSpecBuilder = new ResponseSpecBuilder();
		responseSpecBuilder.expectStatusCode(201).expectContentType(ContentType.JSON).log(LogDetail.ALL);
		responseSpecification = responseSpecBuilder.build();
		return responseSpecification;
	}

	public static RequestSpecification createAddProduct(String url,String productName,String productCategory,String productSubCategory,
			String productPrice,String productDescription,String productFor,String productImge) {
		JsonPath jsonPath = new JsonPath(getResponses().asPrettyString());
		requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.setBaseUri(url).setContentType(ContentType.MULTIPART)
				.addHeader("Authorization", jsonPath.getString("token")).addParam("productName",productName)
				.addParam("productAddedBy", jsonPath.getString("userId")).addParam("productCategory", productCategory)
				.addParam("productSubCategory",productSubCategory).addParam("productPrice", productPrice)
				.addParam("productDescription", productDescription).addParam("productFor", productFor)
				.addMultiPart("productImage", new File(productImge));
		requestSpecification = requestSpecBuilder.build();
		return requestSpecification;
	}
}
