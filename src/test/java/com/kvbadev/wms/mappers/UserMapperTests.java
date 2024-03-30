package com.kvbadev.wms.mappers;

import com.kvbadev.wms.models.security.Role;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.dataTransferObjects.UserDto;
import com.kvbadev.wms.presentation.dataTransferObjects.UserPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.UserView;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.UserMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserMapperTests {
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private static Validator validator;
    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void updateShouldUpdateSetValuesAndIgnoreNullValues() {
        User user = new User("first","lastname","email","password");
        User userUpdate = new User("first2","lastname","email",null);

        userMapper.update(user, userUpdate);

        assert Objects.equals(user.getFirstName(), userUpdate.getFirstName());
        assert user.getPassword() != null;
    }
    @Test
    public void userDtoNotAllowEmailToBeNull() {
        UserDto u = new UserDto("slkdjf","slkdjf",null,null,null);
        Set<ConstraintViolation<UserDto>> errors = validator.validate(u);
        assert !errors.isEmpty();
    }
    @Test
    public void userDtoToUserShouldIgnoreParcelAndId() {
        UserDto userUpdate = new UserDto("first","lastname","email","password", List.of(1,2));
        User user = userMapper.userDtoToUser(userUpdate);

        assert user.getRoles().isEmpty();
        assert user.getId() == null;
    }

    @Test
    public void userPutToUserShouldIgnoreParcelAndUpdateId() {
        UserPutRequest userUpdate = new UserPutRequest(1, "first","lastname","email","password", List.of(1,2));
        User user = userMapper.userPutToUser(userUpdate);

        assert user.getRoles().isEmpty();
        assert user.getId() == 1;
    }
    @Test
    public void userToUserViewMapsRolesToRoleNames() {

        User user = new User("first","lastname","email","password");
        user.setRoles(Set.of(new Role("ADMIN")));
        UserView userView = userMapper.userToUserView(user);

        assert userView.getRoleNames().size() == 1;
    }
}
