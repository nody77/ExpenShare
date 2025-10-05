package com.expenshare.controller;

import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.service.ExpenseService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/expenses")
@Tag(name = "Expenses", description = "Operations related to expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Post
    @Operation(
            summary = "Create a new expense",
            description = "Creates an expense and returns the created expense object."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Expense successfully created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseDto.class)
            )
    )
    HttpResponse<ExpenseDto> createExpense(@Body @Valid CreateExpenseRequest expenseRequest) {
        ExpenseDto newExpense = expenseService.createExpense(expenseRequest);
        return HttpResponse.created(newExpense);
    }


}
