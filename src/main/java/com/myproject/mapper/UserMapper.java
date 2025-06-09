package com.myproject.mapper;

import org.mapstruct.*;
import com.myproject.dto.*;
import com.myproject.model.User;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserBasicDTO toUserBasicDTO(User user);
    UserDetailDTO toUserDetailDTO(User user);

    List<UserBasicDTO> toUserBasicDTOs(List<User> users);
    List<UserDetailDTO> toUserDetailDTOs(List<User> users);

}
