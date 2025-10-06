package com.expenshare;

import com.expenshare.model.dto.user.AddressDto;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class UserControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void testUserCreation(){

        // Prepare Data
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "marwan@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());

        UserDto user = response.getBody(UserDto.class).orElse(null);
        try {
            assertEquals("Marwan Emad", user.getName());
        }
        catch(NullPointerException e){
            throw new RuntimeException("No user returned");
        }

    }

    @Test
    void testCreateInValidUser(){

        // Prepare Data (Invalid Email Format)
        CreateUserRequest requestBodyInvalidEmail = new CreateUserRequest("Marwan Emad", "marwanexample.com");
        HttpRequest<CreateUserRequest> requestInvalidEmail = HttpRequest.POST("/api/users", requestBodyInvalidEmail);
        HttpClientResponseException thrownInvalidEmail = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(requestInvalidEmail)
        );

        assertNotNull(thrownInvalidEmail.getResponse());
        assertEquals(BAD_REQUEST, thrownInvalidEmail.getStatus());


        // Prepare Data (Invalid Phone Format)
        CreateUserRequest requestBodyInvalidPhoneNumber = new CreateUserRequest("Marwan Emad", "marwanexample.com");
        requestBodyInvalidPhoneNumber.setMobileNumber("01234567890");

        HttpRequest<CreateUserRequest> requestInvalidPhoneNumber = HttpRequest.POST("/api/users", requestBodyInvalidPhoneNumber);
        HttpClientResponseException thrownInvalidPhoneNumber = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(requestInvalidPhoneNumber)
        );

        assertNotNull(thrownInvalidPhoneNumber.getResponse());
        assertEquals(BAD_REQUEST, thrownInvalidPhoneNumber.getStatus());
    }

    @Test
    void testCreateUserWithDuplicateEmail(){
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "DuplicateEmail@example.com");
        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());

        HttpClientResponseException thrownInvalidEmail = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(request)
        );

        assertNotNull(thrownInvalidEmail.getResponse());
        assertEquals(CONFLICT, thrownInvalidEmail.getStatus());
    }

    @Test
    void testGetNotFoundUser(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.GET("/api/users/99"))
        );

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testGetExistingUser(){
        // Prepare Data
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "GetExistingUser@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);

        UserDto userCreated = response.getBody(UserDto.class).orElse(null);

        try {
            HttpRequest<Long> getRequest = HttpRequest.GET("/api/users/" + userCreated.getId());
            response = blockingClient.exchange(getRequest);

            UserDto userGet = response.getBody(UserDto.class).orElse(null);

            assertEquals(OK, response.getStatus());

            assertEquals(userCreated.getName(), userGet.getName());

        }
        catch(NullPointerException e){

            throw new RuntimeException("No user returned");
        }
    }
}
