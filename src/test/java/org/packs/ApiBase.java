package org.packs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.testng.annotations.BeforeSuite;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiBase {

	public static Properties properties;
	public static FileInputStream fileInputStream;
	public static RequestSpecBuilder requestSpecBuilder;
	public static RequestSpecification requestSpecification;
	public static ResponseSpecBuilder responseSpecBuilder;
	public static ResponseSpecification responseSpecification;
	public static Response response;

	public static Properties getFileDetails(String fileName) {
		properties = new Properties();
		try {
			fileInputStream = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\java\\org\\lets\\shop\\tests\\" + fileName + ".properties");
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

}
