package com.expenshare.controller;

import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.service.SettlementService;
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
@Controller("/api/settlements")
@Tag(name = "Settlements", description = "Operations related to settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @Post
    @Operation(
            summary = "Create a new Settlement",
            description = "Creates an Settlement and returns the created Settlement object."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Settlement successfully created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SettlementDto.class)
            )
    )
    HttpResponse<SettlementDto> createSettlement(@Body @Valid CreateSettlementRequest req){
        SettlementDto newSettlement = settlementService.createSettlement(req);
        return HttpResponse.created(newSettlement);
    }

    @Post("/{settlementId}/confirm")
    @Operation(
            summary = "Confirm a Pending Settlement",
            description = "Confirm a Pending Settlement using settlement's ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Settlement successfully confirmed",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SettlementDto.class)
            )
    )
    HttpResponse<SettlementDto> confirmSettlement(long settlementId){
        return HttpResponse.ok(settlementService.settlementConfirm(settlementId));
    }

    @Post("/{settlementId}/cancel")
    @Operation(
            summary = "Cancel a Pending Settlement",
            description = "Cancel a Pending Settlement using settlement's ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Settlement successfully canceled",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SettlementDto.class)
            )
    )
    HttpResponse<SettlementDto> cancelSettlement(long settlementId){
        return HttpResponse.ok(settlementService.settlementCancel(settlementId));
    }
}
