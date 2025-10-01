package com.expenshare.service;

import com.expenshare.event.KafkaProducer;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.mapper.UserMapper;
import com.expenshare.repository.facade.UserRepositoryFacade;

import jakarta.inject.Singleton;


@Singleton
public class UserService {

    private final UserRepositoryFacade userRepositoryFacade;
    private final UserMapper userMapper;
    private final KafkaProducer kafkaProducer;


    public UserService(UserRepositoryFacade userRepositoryFacade, UserMapper userMapper, KafkaProducer kafkaProducer) {
        this.userRepositoryFacade = userRepositoryFacade;
        this.userMapper = userMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public UserDto createUser(CreateUserRequest user){

        UserEntity userEntity = userMapper.toEntity(user);

        UserEntity userEntityCreated = userRepositoryFacade.createUser(userEntity.getName(),
                userEntity.getEmail(), userEntity.getMobileNumber(), userEntity.getAddressLine1(), userEntity.getAddressLine2(),
                userEntity.getAddressCity(), userEntity.getAddressState(), userEntity.getAddressPostal(), userEntity.getAddressCountry());

        UserDto newUser = userMapper.toDto(userEntityCreated);
        kafkaProducer.sendUserCreated(newUser);
        kafkaProducer.sendUserWelcomeNotification(newUser);
        return newUser;
    }

    public UserDto getUserByID(long id){
        UserEntity user = userRepositoryFacade.getUserById(id);
        return userMapper.toDto(user);
    }
}
