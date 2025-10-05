package com.expenshare.model.dto.group;

import com.expenshare.model.dto.settlement.SettlementDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.List;

@Introspected
@Serdeable
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GroupDto {

    private long id;
    private String name;
    private List<Long> members;
    private Instant createdAt;
    private List<SettlementDto> items;
    private int page;
    private int size;
    private int total;

    public GroupDto(){}

    public GroupDto(String name, List<Long> members) {
        this.name = name;
        this.members = members;
    }

    public GroupDto(long id, List<SettlementDto> items,int page, int size, int total){
        this.id = id;
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<SettlementDto> getItems() {
        return items;
    }

    public void setItems(List<SettlementDto> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
