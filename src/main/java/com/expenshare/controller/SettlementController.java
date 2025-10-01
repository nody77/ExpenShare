package com.expenshare.controller;

import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.service.SettlementService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;

import java.util.Map;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @Post
    SettlementDto createSettlement(@Body @Valid CreateSettlementRequest req){
        return settlementService.createSettlement(req);
    }

    @Post("/{settlementId}/confirm")
    Map<String, String> confirmSettlement(long settlementId){
        return settlementService.settlementConfirm(settlementId);
    }

    @Post("/{settlementId}/cancel")
    Map<String, String> cancelSettlement(long settlementId){
        return settlementService.settlementCancel(settlementId);
    }
}
