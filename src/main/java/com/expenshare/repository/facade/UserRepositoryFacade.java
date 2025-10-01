package com.expenshare.repository.facade;

import com.expenshare.exception.ConflictException;
import com.expenshare.exception.NotFoundException;
import com.expenshare.exception.ValidationException;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.enums.CountryISO;
import com.expenshare.repository.UserRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.exception.ConstraintViolationException;


@Singleton
public class UserRepositoryFacade implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public UserEntity createUser(@NotBlank String name, @NotBlank String email, @Nullable String mobileNumber,
                          @Nullable String addressLine1, @Nullable String addressLine2, @Nullable String addressCity,
                          @Nullable String addressState, @Nullable String addressPostal,
                          @Nullable CountryISO addressCountry){
        try{
            UserEntity newUser = new UserEntity(name, email, mobileNumber, addressLine1, addressLine2, addressCity,
                    addressState, addressPostal,addressCountry);
            entityManager.persist(newUser);
            entityManager.flush();
            return newUser;
        }
        catch(PersistenceException e){
            if (isConstraintViolationException(e)) {
                ConstraintViolationException cve = (ConstraintViolationException) e;
                String constraintName = cve.getConstraintName();
                if ("PUBLIC.UK_USER_EMAIL_INDEX_4".equals(constraintName)) {
                    throw new ConflictException("Email already exists");
                }
            }
            throw e;
        }
        catch (jakarta.validation.ConstraintViolationException e){
            throw new ValidationException("Invalid email or mobileNumber");
        }
    }

    @Override
    @Transactional
    public UserEntity getUserById(long id){
        UserEntity user = entityManager.find(UserEntity.class, id);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        return user;
    }

    private boolean isConstraintViolationException(Throwable e) {
        while (e != null) {
            if (e instanceof ConstraintViolationException) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }


}
