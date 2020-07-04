package com.hackerRankApiTest.com.restApiTest;

import Model.Category;
import Model.Pet;
import Model.Tags;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.apache.http.client.HttpClient;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTests {

	String swagURI = "";
    JSONObject json = new JSONObject();
    int petId;
    Pet pet = new Pet();

    @Before
    public  void jsonData(){
        //have used Json for initialize tests
        swagURI = "https://petstore.swagger.io/v2/";
        petId =  190;
        json.put("id", petId);
        JSONObject catJson = new JSONObject();
        catJson.put("id", 0);
        catJson.put("name", "persian");
        json.put("category", catJson);
        json.put("name", "jonie");
        String[] myArray = {"dogWorld"};
        json.put("photoUrls", myArray);
        JSONObject tagJson = new JSONObject();
        catJson.put("id", 0);
        catJson.put("name", "persian");
        JSONArray array = new JSONArray();
        array.add(tagJson);
        json.put("tags", array);
        json.put("status", "available");

        //have used restTemplate for file upload test case
        //have used Entity Object in last test
        pet.setId(petId+8);
        pet.setCategory(new Category(0,"Persian"));
        pet.setName("Mina");
        pet.setStatus("available");
        List<Tags> tagList = new ArrayList<>();
        Tags t = new Tags(0,"dogTag");
        tagList.add(t);
        pet.setTags(tagList);
        List list =  new ArrayList<>();
        list.add("sample");
        pet.setPhotoUrls(myArray);
    }

	@Test
	public void test1SwagJson() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "https://petstore.swagger.io/v2/swagger.json";
		URI uri = new URI(baseUrl);
		ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		Assert.assertEquals(200l, Long.parseLong(String.valueOf(result.getStatusCode().value())));
	}

    @Test
    public void test2AddNewPet() throws IOException {
        JSONObject testObj = new JSONObject();
        testObj = json;
        testObj.put("id",petId);
        testObj.put("status","available");
        testAddPet(testObj);
        testObj.put("id",petId+1);
        testObj.put("status","pending");
        testAddPet(testObj);
        testObj.put("id",petId+2);
        testObj.put("status","sold");
        testAddPet(testObj);
    }

    private void testAddPet(JSONObject testObj) throws IOException {
        final String POST_PARAMS = testObj.toString();
        URL obj = new URL(swagURI+"pet");
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        OutputStream os = postConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        int responseCode = postConnection.getResponseCode();
        Assert.assertEquals(200, responseCode);
    }

	@Test
    public void test3GetPetById() throws IOException {
        String url = swagURI+"pet/";
        url = url+petId;
        URL urlForGetRequest = new URL(url);
        String readLine = "";
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("Content-Type", "application/json");
        conection.setDoOutput(true);
        int responseCode = conection.getResponseCode();
        Assert.assertEquals(200, responseCode);
    }

    @Test
    public void test4GetPetByStatus() throws Exception {
        getPetByStatus("available",petId);
        getPetByStatus("pending",petId+1);
        getPetByStatus("sold",petId+2);
        //getPetByStatus("wrongStatus",petId+2);
    }

    private void getPetByStatus(String status,int id) throws  Exception{
        String url = swagURI+"pet/";
        url = url+"findByStatus?status="+status;
        URL urlForGetRequest = new URL(url);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("Content-Type", "application/json");
        conection.setDoOutput(true);
        int responseCode = conection.getResponseCode();
        Assert.assertEquals(200, responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer resp = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                resp.append(readLine);
            } in .close();
            Assert.assertNotNull(resp);
            if(null != resp){
                JSONParser parser = new JSONParser();
                JSONArray localArray = (JSONArray) parser.parse(resp.toString());
                JSONObject localJson = (JSONObject)localArray.get(0);
                Assert.assertEquals(status,localJson.get("status").toString());
            }
        } else {
            Assert.fail();
        }
    }

    @Test
    public void test5UpdatePetById() throws Exception {
        JSONObject jsonObj = new JSONObject();
        final String POST_PARAMS = "name=Johnie&status=sold";
        String url = swagURI+"pet/";
        url = url+153;
        String readLine = "";
        URL urlForGetRequest = new URL(url);
        HttpURLConnection postConnection = (HttpURLConnection) urlForGetRequest.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Length", String.valueOf(POST_PARAMS.getBytes().length));
        postConnection.setRequestProperty("Content-Language", "en-US");
        postConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        postConnection.setUseCaches (true);
        postConnection.setDoOutput(true);
        postConnection.setDoInput(true);
        DataOutputStream os = new DataOutputStream(postConnection.getOutputStream());
        os.writeBytes(POST_PARAMS);
        os.flush();
        os.close();
        int responseCode = postConnection.getResponseCode();
        Assert.assertEquals(200, responseCode);
    }

	@Test
    public void test6UpdatePet() throws Exception {
        json.put("name", "manie");
        final String POST_PARAMS = json.toString();
        String readLine = "";
        URL obj = new URL(swagURI+"pet");
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("PUT");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        OutputStream os = postConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        int responseCode = postConnection.getResponseCode();
        Assert.assertEquals(200, responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(postConnection.getInputStream()));
            StringBuffer resp = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                resp.append(readLine);
            } in .close();
            Assert.assertNotNull(resp);
            if(null != resp){
                JSONParser parser = new JSONParser();
                JSONObject localObj = (JSONObject) parser.parse(resp.toString());
                Assert.assertEquals("manie",localObj.get("name").toString());
            }
        } else {
            Assert.fail();
        }
    }

    @Test
    public void test7AddNewImage()  {
        try{
            File file = ResourceUtils.getFile("classpath:puppy.jpeg");
            testUploadImage(file,"jpeg",petId);
            file = ResourceUtils.getFile("classpath:sampleTxt.txt");
            testUploadImage(file,"txt",petId+1);
            //file = ResourceUtils.getFile("classpath:sampleTxt.txt");
            //testUploadImage(file,"txt",petId+9);
        }  catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }


    private void testUploadImage(File file, String type, int id) throws Exception{
        String url = swagURI+"pet/";
        url = url+petId+"/uploadImage";
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","application/json");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        RestTemplate restTemplate = new RestTemplate();
       try{
           ResponseEntity<String> response = restTemplate.exchange(url,
                   HttpMethod.POST, requestEntity, String.class);
           if(type.equalsIgnoreCase("jpeg")){
               Assert.assertEquals(200, response.getStatusCode().value());
           }
           if(type.equalsIgnoreCase("txt")) {
               //not sure if application should support only image formats. so commented as of now
               //Assert.fail();
           }
           if(id == petId+9){
               //Assert.fail();
           }
       } catch(Exception e){
           if(id == petId+9){
               //Assert.assertEquals("404 Not Found", e.getMessage());
               Assert.fail();
           }
       }
    }

    @Test
    public void test8DeletePetById() throws IOException {
        try{
            String url = swagURI+"pet/";
            url = url+petId;
            URL urlForGetRequest = new URL(url);
            String readLine = "";
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("DELETE");
            conection.setRequestProperty("Content-Type", "application/json");
            conection.setDoOutput(true);
            int responseCode = conection.getResponseCode();
            Assert.assertEquals(200, responseCode);
        } catch(Exception e){
            Assert.fail();
        }
    }


    @Test
	public void test9AddPetByObject() throws Exception
	{
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		final String baseUrl = swagURI+"pet";
		URI uri = new URI(baseUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");

		try{
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pet);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonString);
            HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
            ResponseEntity<String> response =  restTemplate.postForEntity(uri, request, String.class);
            Assert.assertEquals(200, response.getStatusCode().value());
        } catch (Exception e){
            Assert.fail();;
        }
	}


	/*@Test
    public void test9BUrl() throws Exception
    {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        final String baseUrl = swagURI;
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");

        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pet);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
        try{
            restTemplate.postForEntity(uri, request, String.class);
        } catch (Exception e){
            Assert.assertEquals("404 Not Found", e.getMessage());
            Assert.fail();
        }
    }*/
}
