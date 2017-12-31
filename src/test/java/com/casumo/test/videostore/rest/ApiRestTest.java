package com.casumo.test.videostore.rest;

import com.casumo.test.videostore.VideoStoreApplication;
import com.casumo.test.videostore.controller.dto.CustomerDto;
import com.casumo.test.videostore.controller.dto.FilmDto;
import com.casumo.test.videostore.controller.dto.RentFilmsDto;
import com.casumo.test.videostore.controller.dto.ReturnFilmsDto;
import com.casumo.test.videostore.entity.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.casumo.test.videostore.acceptance.VideoStoreTestConstants.*;
import static com.casumo.test.videostore.constants.VideoStoreConstants.SECURITY_TOKEN_TESTING;
import static java.lang.Thread.sleep;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = VideoStoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiRestTest{

    private static final Logger logger = LoggerFactory.getLogger(ApiRestTest.class);

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    private String customerId;

    private String matrixFilmId, spiderManFilmId, spiderMan2FilmId, outOfAfricaFilmId;


    @Test
    public void testRetrieveCustomers() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/customers"), HttpMethod.GET, entity, String.class);

        try {
            new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Customer>>(){});
        } catch (IOException e) {
            throw new AssertionError("Can't read the response of /customers endpoint");
        }
    }

    @Test
    public void testRetrieveFilms() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/films"), HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<FilmDto> films = mapper.readValue(response.getBody(), new TypeReference<List<FilmDto>>(){});
            films.forEach(filmDto -> Assert.assertFalse(filmDto.getTitle().isEmpty()));
        } catch (IOException e) {
            throw new AssertionError("Can't read the response of /films endpoint");
        }
    }

    @Test
    public void testACustomerRentAFilm() throws InterruptedException {
        setUpForRent();

        HttpEntity<RentFilmsDto> entity = new HttpEntity<>(new RentFilmsDto(customerId, singletonList(matrixFilmId), 1), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity, String.class);
        assertEquals("40", response.getBody());

        HttpEntity<RentFilmsDto> entity2 = new HttpEntity<>(new RentFilmsDto(customerId, singletonList(spiderManFilmId), 5), headers);
        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity2, String.class);
        assertEquals("90", response2.getBody());

        HttpEntity<RentFilmsDto> entity3 = new HttpEntity<>(new RentFilmsDto(customerId, singletonList(spiderMan2FilmId), 2), headers);
        ResponseEntity<String> response3 = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity3, String.class);
        assertEquals("30", response3.getBody());

        HttpEntity<RentFilmsDto> entity4 = new HttpEntity<>(new RentFilmsDto(customerId, singletonList(outOfAfricaFilmId), 7), headers);
        ResponseEntity<String> response4 = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity4, String.class);
        assertEquals("90", response4.getBody());
    }

    @Test
    public void testACustomerReturnAFilm() throws InterruptedException {
        setUpForRent();

        restTemplate.exchange(createURLWithPort("/testutils/fixdate/2017-01-01/" + SECURITY_TOKEN_TESTING), HttpMethod.POST,  new HttpEntity<>(headers), String.class);

        HttpEntity<RentFilmsDto> entity2 = new HttpEntity<>(new RentFilmsDto(customerId, singletonList(matrixFilmId), 1), headers);
        restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity2, String.class);
        sleep(100); //TODO: See how we can improve it to know when the data is actually stored

        restTemplate.exchange(createURLWithPort("/testutils/fixdate/2017-01-04/" + SECURITY_TOKEN_TESTING), HttpMethod.POST,  new HttpEntity<>(headers), String.class);

        HttpEntity<ReturnFilmsDto> entity4 = new HttpEntity<>(new ReturnFilmsDto(customerId, singletonList(matrixFilmId)), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/videostore/return"), HttpMethod.POST, entity4, String.class);
        assertEquals("80", response.getBody());

    }

    private void setUpForRent() throws InterruptedException {
        //Create Customer
        HttpEntity<CustomerDto> entity1 = new HttpEntity<>(new CustomerDto("John Carter", 123469587), headers);
        ResponseEntity<String> response1 = restTemplate.exchange(createURLWithPort("/customer"), HttpMethod.POST, entity1, String.class);
        customerId = response1.getBody();

        //Create films
        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, new HttpEntity<>(matrixFilm, headers), String.class);
        matrixFilmId = response2.getBody();

        ResponseEntity<String> response3 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, new HttpEntity<>(spiderManFilm, headers), String.class);
        spiderManFilmId = response3.getBody();

        ResponseEntity<String> response4 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, new HttpEntity<>(spiderMan2Film, headers), String.class);
        spiderMan2FilmId = response4.getBody();

        ResponseEntity<String> response5 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, new HttpEntity<>(outOfAfricaFilm, headers), String.class);
        outOfAfricaFilmId = response5.getBody();

        sleep(100);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}