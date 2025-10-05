package com.expenshare;


import com.expenshare.model.dto.user.AddressDto;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class UserControllerTest {

    /*private BlockingHttpClient blockingClient;

    @Inject
    @Client("/api")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void testCreateNewUser(){

        CreateUserRequest userRequest = new CreateUserRequest("Ahmed", "ahmed@gmail.com");
        userRequest.setMobileNumber("+201234567890");
        userRequest.setAddress(new AddressDto("123 King Fahd Rd"));
        userRequest.getAddress().setCity("Riyadh");
        userRequest.getAddress().setPostalCode("11564");
        userRequest.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", userRequest);

        HttpResponse<UserDto> response = blockingClient.exchange(request);
        assertEquals(CREATED, response.getStatus());

        UserDto newUser = blockingClient.retrieve(request, UserDto.class);
        assertEquals("Ahmed", newUser.getName());
        assertEquals("ahmed@gmail.com", newUser.getEmail());
        assertEquals("+201234567890", newUser.getMobileNumber());
        assertEquals("123 King Fahd Rd", newUser.getAddress().getLine1());
        assertEquals("Riyadh", newUser.getAddress().getCity());
        assertEquals("11564", newUser.getAddress().getPostalCode());
        assertEquals("SA", newUser.getAddress().getCountry());
    }

    @Test
    void testGetExistingUser(){

    }

    @Test
    void testGetNonExistingUser(){

    }*/

}
