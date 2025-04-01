package ru.kstn.taskmanagementsystem.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.auth.dto.RegisterRequest;
import ru.kstn.taskmanagementsystem.user.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
    User toUser(RegisterRequest request);

}
