package com.smartvocab.smart_vocab_backend.mapper;

import com.smartvocab.smart_vocab_backend.dto.user.UserBasicInfo;
import com.smartvocab.smart_vocab_backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserBasicInfo toDto(User user);
}
