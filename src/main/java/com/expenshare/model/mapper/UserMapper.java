package com.expenshare.model.mapper;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jsr330")
public interface UserMapper {


    @Mapping(target = "name" , source = "dto.name")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "mobileNumber", source = "dto.mobileNumber")
    @Mapping(target = "addressLine1", source = "dto.address.line1")
    @Mapping(target = "addressLine2", source = "dto.address.line2")
    @Mapping(target = "addressCity", source = "dto.address.city")
    @Mapping(target = "addressState", source = "dto.address.state")
    @Mapping(target = "addressPostal", source = "dto.address.postalCode")
    @Mapping(target = "addressCountry", expression = "java(dto.getAddress() != null ? com.expenshare.model.enums.CountryISO.valueOf(dto.getAddress().getCountry()) : null)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupMemberEntities", ignore = true)
    @Mapping(target = "expenseEntities", ignore = true)
    @Mapping(target = "expenseShareEntities", ignore = true)
    @Mapping(target = "settlementMade", ignore = true)
    @Mapping(target = "settlementReceived", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toEntity(CreateUserRequest dto);


    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "mobileNumber", source = "user.mobileNumber")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "address.line1", source = "user.addressLine1")
    @Mapping(target = "address.line2", source = "user.addressLine2")
    @Mapping(target = "address.city", source = "user.addressCity")
    @Mapping(target = "address.state", source = "user.addressState")
    @Mapping(target = "address.postalCode", source = "user.addressPostal")
    @Mapping(target = "address.country", source = "user.addressCountry")
    UserDto toDto(UserEntity user);

}
