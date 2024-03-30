package com.kvbadev.wms;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.models.security.Role;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryDto;
import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.presentation.dataTransferObjects.UserView;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.ItemMapper;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class ObjectMapperTests {
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final ItemMapper itemMapper = ItemMapper.INSTANCE;

    @Test
    public void mapUserToUserViewChangesRolesSetToRolesNamesSet() throws Exception {
        User user = new User("fisrt","lsdjf","flskdjfls@lsjf.com","dlfsjkdlfskd");
        user.setRoles(new HashSet<>(List.of(new Role("admin"), new Role("staff"))));
        UserView uv = userMapper.userToUserView(user);
        assert uv.getRoleNames().equals(new HashSet<>(List.of("admin", "staff")));
    }
}
