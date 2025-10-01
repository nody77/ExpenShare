package com.expenshare.repository;

import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.enums.CountryISO;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository {

    UserEntity createUser(@NotBlank String name, @NotBlank String email, @Nullable String mobileNumber,
                          @Nullable String addressLine1,@Nullable String addressLine2,@Nullable String addressCity,
                          @Nullable String addressState,@Nullable String addressPostal,
                          @Nullable CountryISO addressCountry);

    UserEntity getUserById(long id);
}
