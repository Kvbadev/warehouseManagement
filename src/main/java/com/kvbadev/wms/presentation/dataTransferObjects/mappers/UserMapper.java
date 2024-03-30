package com.kvbadev.wms.presentation.dataTransferObjects.mappers;

import com.kvbadev.wms.models.security.Role;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.dataTransferObjects.UserDto;
import com.kvbadev.wms.presentation.dataTransferObjects.UserPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.UserView;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget User user, User updateUser);

    @Mapping(target = "roles", ignore = true)
    User userDtoToUser(UserDto userDto);

    @Mapping(target = "roles", ignore = true)
    User userPutToUser(UserPutRequest userPut);

    @Mapping(source = "roles", target = "roleNames")
    UserView userToUserView(User user);

    default Set<String> rolesToRoleNames(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
