package ru.kstn.taskmanagementsystem.mappers.auth;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.dtos.auth.RegisterRequest;
import ru.kstn.taskmanagementsystem.entities.user.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
    User toUser(RegisterRequest request);

}
