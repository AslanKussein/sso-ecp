package kz.crtr.service;

import kz.crtr.dto.UserDto;

public interface UserService {
    UserDto findUserByName(final String username);
}
