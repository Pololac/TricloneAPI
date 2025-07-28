package com.hb.cda.examapi.controller.dto.mapper;

import com.hb.cda.examapi.controller.dto.UserDTO;
import com.hb.cda.examapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toUserDTO(User user);
}
