package ru.kstn.taskmanagementsystem.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.user.dto.SimpleUserDto;
import ru.kstn.taskmanagementsystem.user.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SimpleUserMapper {
    SimpleUserDto toSimpleUserDto(User user);
}
