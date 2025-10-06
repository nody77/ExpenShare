package com.expenshare;

import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.dto.user.AddressDto;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.enums.ExpenseSplitType;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ExpenseControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void testCreatingExpenseEqual(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user89@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user80@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);


        HttpRequest<CreateExpenseRequest> expenseRequest = HttpRequest.POST("/api/expenses",
                new CreateExpenseRequest(groupCreated.getId(), userOne.getId(), BigDecimal.valueOf(1000),
                        "Dinner", ExpenseSplitType.EQUAL));

        HttpResponse<ExpenseDto> expenseResponse = blockingClient.exchange(expenseRequest);
        ExpenseDto expense = expenseResponse.getBody(ExpenseDto.class).orElse(null);

        assertEquals(CREATED, expenseResponse.getStatus());
        assertEquals("-500.00", expense.getSplit().get(0).getAmount().toString());
        assertEquals("500.00", expense.getSplit().get(1).getAmount().toString());

    }

    @Test
    void testCreatingExpenseExact(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user70@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user81@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);


        CreateExpenseRequest expenseRequestDTO = new CreateExpenseRequest(groupCreated.getId(), userOne.getId(), BigDecimal.valueOf(1000.0),
                "Dinner", ExpenseSplitType.EXACT);
        List<ShareDto> shares = new ArrayList<>();
        shares.add(new ShareDto(userOne.getId(), BigDecimal.valueOf(750.0)));
        shares.add(new ShareDto(userTwo.getId(), BigDecimal.valueOf(250.0)));
        expenseRequestDTO.setSplit(shares);
        HttpRequest<CreateExpenseRequest> expenseRequest = HttpRequest.POST("/api/expenses", expenseRequestDTO);

        HttpResponse<ExpenseDto> expenseResponse = blockingClient.exchange(expenseRequest);
        ExpenseDto expense = expenseResponse.getBody(ExpenseDto.class).orElse(null);

        assertEquals(CREATED, expenseResponse.getStatus());
        assertEquals("-250.0", expense.getSplit().get(0).getAmount().toString());
        assertEquals("250.0", expense.getSplit().get(1).getAmount().toString());
    }

    @Test
    void testCreatingExpensePercent(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user77@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user51@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);


        CreateExpenseRequest expenseRequestDTO = new CreateExpenseRequest(groupCreated.getId(), userOne.getId(), BigDecimal.valueOf(1000.0),
                "Dinner", ExpenseSplitType.PERCENT);
        List<ShareDto> shares = new ArrayList<>();
        shares.add(new ShareDto(userOne.getId(), BigDecimal.valueOf(25)));
        shares.add(new ShareDto(userTwo.getId(), BigDecimal.valueOf(75)));
        expenseRequestDTO.setSplit(shares);
        HttpRequest<CreateExpenseRequest> expenseRequest = HttpRequest.POST("/api/expenses", expenseRequestDTO);

        HttpResponse<ExpenseDto> expenseResponse = blockingClient.exchange(expenseRequest);
        ExpenseDto expense = expenseResponse.getBody(ExpenseDto.class).orElse(null);

        assertEquals(CREATED, expenseResponse.getStatus());
        assertEquals("-750.00", expense.getSplit().get(0).getAmount().toString());
        assertEquals("750.00", expense.getSplit().get(1).getAmount().toString());
    }

    @Test
    void testSplitValueNotCorrectExact(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user75@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user88@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);


        CreateExpenseRequest expenseRequestDTO = new CreateExpenseRequest(groupCreated.getId(), userOne.getId(), BigDecimal.valueOf(1000.0),
                "Dinner", ExpenseSplitType.EXACT);
        List<ShareDto> shares = new ArrayList<>();
        shares.add(new ShareDto(userOne.getId(), BigDecimal.valueOf(950.0)));
        shares.add(new ShareDto(userTwo.getId(), BigDecimal.valueOf(250.0)));
        expenseRequestDTO.setSplit(shares);
        HttpRequest<CreateExpenseRequest> expenseRequest = HttpRequest.POST("/api/expenses", expenseRequestDTO);

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(expenseRequest));

        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void testSplitValueNotCorrectPercent(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user79@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user56@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);


        CreateExpenseRequest expenseRequestDTO = new CreateExpenseRequest(groupCreated.getId(), userOne.getId(), BigDecimal.valueOf(1000.0),
                "Dinner", ExpenseSplitType.PERCENT);
        List<ShareDto> shares = new ArrayList<>();
        shares.add(new ShareDto(userOne.getId(), BigDecimal.valueOf(35)));
        shares.add(new ShareDto(userTwo.getId(), BigDecimal.valueOf(75)));
        expenseRequestDTO.setSplit(shares);
        HttpRequest<CreateExpenseRequest> expenseRequest = HttpRequest.POST("/api/expenses", expenseRequestDTO);

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(expenseRequest));

        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void testGetExistingExpense(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user888@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user952@example.com");
        request = HttpRequest.POST("/api/users", requestBody);
        response = blockingClient.exchange(request);
        UserDto userTwo = response.getBody(UserDto.class).orElse(null);

        List<Long> membersID = new ArrayList<>();
        membersID.add(userOne.getId());
        membersID.add(userTwo.getId());
        CreateGroupRequest groupRequestBody = new CreateGroupRequest("Trip to Dubai", membersID);

        HttpRequest<CreateGroupRequest> createGroupRequest = HttpRequest.POST("/api/groups", groupRequestBody);
        HttpResponse<GroupDto> createGroupresponse = blockingClient.exchange(createGroupRequest);

        GroupDto groupCreated = createGroupresponse.getBody(GroupDto.class).orElse(null);


        HttpRequest<CreateExpenseRequest> expenseRequest = HttpRequest.POST("/api/expenses",
                new CreateExpenseRequest(groupCreated.getId(), userOne.getId(), BigDecimal.valueOf(1000),
                        "Dinner", ExpenseSplitType.EQUAL));

        HttpResponse<ExpenseDto> expenseResponse = blockingClient.exchange(expenseRequest);

        HttpRequest<Long> getGroupBalanceRequest = HttpRequest.GET("/api/groups/"+groupCreated.getId()+"/balances");
        HttpResponse<ExpenseDto> groupBalanceResponse = blockingClient.exchange(getGroupBalanceRequest);



        assertEquals(OK, groupBalanceResponse.getStatus());


    }

}
