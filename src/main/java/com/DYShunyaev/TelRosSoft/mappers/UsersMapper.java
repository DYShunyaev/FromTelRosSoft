package com.DYShunyaev.TelRosSoft.mappers;

import com.DYShunyaev.TelRosSoft.dto.UsersDTO;
import com.DYShunyaev.TelRosSoft.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsersMapper {

    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UsersDTO toDTO(Users user);
}
