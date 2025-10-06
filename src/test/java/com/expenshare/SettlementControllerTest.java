package com.expenshare;

import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.dto.user.AddressDto;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.enums.ExpenseSplitType;
import com.expenshare.model.enums.SettlementMethod;
import com.expenshare.model.enums.SettlementStatus;
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
public class SettlementControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }


    @Test
    void testCreateSettlement(){
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


        HttpRequest<CreateSettlementRequest> settlementRequest = HttpRequest.POST("/api/settlements", new CreateSettlementRequest(groupCreated.getId(), userTwo.getId(), userOne.getId(), BigDecimal.valueOf(500)));
        HttpResponse<SettlementDto> settlementResponse = blockingClient.exchange(settlementRequest);
        SettlementDto settlement = settlementResponse.getBody(SettlementDto.class).orElse(null);
        assertEquals(CREATED, settlementResponse.getStatus());
        assertEquals(SettlementMethod.OTHER, settlement.getMethod());
        assertEquals(SettlementStatus.PENDING, settlement.getStatus());
    }

    @Test
    void testCreateSettlementWithInvalidAmount(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user555@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user953@example.com");
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


        HttpRequest<CreateSettlementRequest> settlementRequest = HttpRequest.POST("/api/settlements", new CreateSettlementRequest(groupCreated.getId(), userTwo.getId(), userOne.getId(), BigDecimal.valueOf(6000)));
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(settlementRequest));

        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void testConfirmPendingSettlement(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user855@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user958@example.com");
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


        HttpRequest<CreateSettlementRequest> settlementRequest = HttpRequest.POST("/api/settlements", new CreateSettlementRequest(groupCreated.getId(), userTwo.getId(), userOne.getId(), BigDecimal.valueOf(500)));
        HttpResponse<SettlementDto> settlementResponse = blockingClient.exchange(settlementRequest);
        SettlementDto settlement = settlementResponse.getBody(SettlementDto.class).orElse(null);

        String confirmURI = "/api/settlements/" + settlement.getSettlementId()+ "/confirm";

        HttpRequest<Long> confirmSettlementRequest = HttpRequest.POST(confirmURI, null);
        HttpResponse<SettlementDto> confirmedSettlementResponse = blockingClient.exchange(confirmSettlementRequest);
        SettlementDto confirmedSettlement = confirmedSettlementResponse.getBody(SettlementDto.class).orElse(null);

        assertEquals(OK, confirmedSettlementResponse.getStatus());
        assertEquals("CONFIRMED", confirmedSettlement.getStatus().toString());

    }

    @Test
    void testCancelPendingSettlement(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user878@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user972@example.com");
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


        HttpRequest<CreateSettlementRequest> settlementRequest = HttpRequest.POST("/api/settlements", new CreateSettlementRequest(groupCreated.getId(), userTwo.getId(), userOne.getId(), BigDecimal.valueOf(500)));
        HttpResponse<SettlementDto> settlementResponse = blockingClient.exchange(settlementRequest);
        SettlementDto settlement = settlementResponse.getBody(SettlementDto.class).orElse(null);

        String confirmURI = "/api/settlements/" + settlement.getSettlementId()+ "/cancel";

        HttpRequest<Long> confirmSettlementRequest = HttpRequest.POST(confirmURI, null);
        HttpResponse<SettlementDto> confirmedSettlementResponse = blockingClient.exchange(confirmSettlementRequest);
        SettlementDto confirmedSettlement = confirmedSettlementResponse.getBody(SettlementDto.class).orElse(null);


        assertEquals(OK, confirmedSettlementResponse.getStatus());
        assertEquals("CANCELED", confirmedSettlement.getStatus().toString());
    }

    @Test
    void testConfirmConfirmedSettlement(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user8188@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user951@example.com");
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


        HttpRequest<CreateSettlementRequest> settlementRequest = HttpRequest.POST("/api/settlements", new CreateSettlementRequest(groupCreated.getId(), userTwo.getId(), userOne.getId(), BigDecimal.valueOf(500)));
        HttpResponse<SettlementDto> settlementResponse = blockingClient.exchange(settlementRequest);
        SettlementDto settlement = settlementResponse.getBody(SettlementDto.class).orElse(null);

        String confirmURI = "/api/settlements/" + settlement.getSettlementId()+ "/confirm";

        HttpRequest<Long> confirmSettlementRequest = HttpRequest.POST(confirmURI, null);
        HttpResponse<SettlementDto> confirmedSettlementResponse = blockingClient.exchange(confirmSettlementRequest);

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(confirmSettlementRequest));

        assertNotNull(thrown.getResponse());
        assertEquals(CONFLICT, thrown.getStatus());
    }

    @Test
    void testCancelConfirmedSettlement(){
        // Prepare Data User 1
        CreateUserRequest requestBody = new CreateUserRequest("Marwan Emad", "user818@example.com");
        requestBody.setMobileNumber("+201234567890");
        requestBody.setAddress(new AddressDto("123 King Fahd Rd"));
        requestBody.getAddress().setCity("Riyadh");
        requestBody.getAddress().setPostalCode("11564");
        requestBody.getAddress().setCountry("SA");

        HttpRequest<CreateUserRequest> request = HttpRequest.POST("/api/users", requestBody);
        HttpResponse<UserDto> response = blockingClient.exchange(request);
        UserDto userOne = response.getBody(UserDto.class).orElse(null);

        // Prepare Data User 2
        requestBody.setEmail("user59@example.com");
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


        HttpRequest<CreateSettlementRequest> settlementRequest = HttpRequest.POST("/api/settlements", new CreateSettlementRequest(groupCreated.getId(), userTwo.getId(), userOne.getId(), BigDecimal.valueOf(500)));
        HttpResponse<SettlementDto> settlementResponse = blockingClient.exchange(settlementRequest);
        SettlementDto settlement = settlementResponse.getBody(SettlementDto.class).orElse(null);

        String confirmURI = "/api/settlements/" + settlement.getSettlementId()+ "/cancel";

        HttpRequest<Long> confirmSettlementRequest = HttpRequest.POST(confirmURI, null);
        HttpResponse<SettlementDto> confirmedSettlementResponse = blockingClient.exchange(confirmSettlementRequest);

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(confirmSettlementRequest));

        assertNotNull(thrown.getResponse());
        assertEquals(CONFLICT, thrown.getStatus());
    }

    @Test
    void testConfirmNonExistingSettlement(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.POST("/api/settlements/999999/confirm",null)));

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testCancelNonExistingSettlement(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.POST("/api/settlements/999999/cancel",null)));

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }

}
