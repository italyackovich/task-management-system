package ru.kstn.taskmanagementsystem.mappers.user;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.dtos.user.SimpleUserDto;
import ru.kstn.taskmanagementsystem.entities.user.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SimpleUserMapper {
    SimpleUserDto toSimpleUserDto(User user);
}
