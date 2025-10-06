package com.expenshare;

import com.expenshare.model.dto.group.AddMembersRequest;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
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

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class GroupControllerTest {


    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void testGroupCreation(){

        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user18@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());

        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user28@example.com");

        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());

        UserDto userTwo = response.getBody(UserDto.class).orElse(null);


        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);

        assertEquals(CREATED, createGroupresponse.getStatus());
        assertEquals("Trip to Dubai", groupCreated.getName());

    }

    @Test
    void testInvalidGroupCreation(){
        List<Long> membersID = new ArrayList<>();
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);

        HttpClientResponseException thrownInvalidGroupCreation = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(createGroupRequest)
        );
        assertNotNull(thrownInvalidGroupCreation.getResponse());
        assertEquals(BAD_REQUEST, thrownInvalidGroupCreation.getStatus());
    }

    @Test
    void testInvalidGroupCreationUserNotFound(){
        List<Long> membersID = new ArrayList<>();
        membersID.add(9999L);
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);

        HttpClientResponseException thrownInvalidGroupCreation = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(createGroupRequest)
        );
        assertNotNull(thrownInvalidGroupCreation.getResponse());
        assertEquals(NOT_FOUND, thrownInvalidGroupCreation.getStatus());
    }

    @Test
    void testGetNonExistingGroup(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.GET("/api/groups/99"))
        );

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testGetExisting(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user11@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());

        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user21@example.com");

        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());

        UserDto userTwo = response.getBody(UserDto.class).orElse(null);


        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);

        assertEquals(CREATED, createGroupresponse.getStatus());
        assertEquals("Trip to Dubai", groupCreated.getName());


        HttpResponse<GroupDto> getResponse = blockingClient.exchange(HttpRequest.GET("/api/groups/" + groupCreated.getId()));
        GroupDto group = getResponse.getBody(GroupDto.class).orElse(null);

        assertEquals(OK, getResponse.getStatus());
        assertEquals("Trip to Dubai", group.getName());
    }

    @Test
    void testAddMemberToExistingGroup(){

        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user1@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user2@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 3
        requestBody.setEmail("user7@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userThree = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);

        membersID.remove(userOne.getId());
        membersID.remove(userTwo.getId());

        membersID.add(userThree.getId());
        AddMembersRequest addMembersRequest = new AddMembersRequest(membersID);
        HttpRequest<AddMembersRequest> addMembersRequestHttp = HttpRequest.POST("/api/groups/" + groupCreated.getId() + "/members", addMembersRequest);
        HttpResponse<AddMembersRequest> addMembersRequestHttpResponse = blockingClient.exchange(addMembersRequestHttp);
        AddMembersRequest addMembersResponse = addMembersRequestHttpResponse.getBody(AddMembersRequest.class).orElse(null);

        assertEquals(OK, addMembersRequestHttpResponse.getStatus());
        assertEquals(3, addMembersResponse.getTotalMember());
    }

    @Test
    void testAddMemberToNonExistingGroup(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user111@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user222@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());

        AddMembersRequest addMembersRequest = new AddMembersRequest(membersID);
        HttpRequest<AddMembersRequest> addMembersRequestHttp = HttpRequest.POST("/api/groups/9999/members", addMembersRequest);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(addMembersRequestHttp)
        );

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }

}
