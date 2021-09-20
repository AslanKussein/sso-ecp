package kz.mtszn.service;

import kz.mtszn.dto.UserDto;
import kz.mtszn.models.BlockUser;

import java.util.List;

public interface UserService {
    UserDto findUserByName(final String username);

    List<BlockUser> getListBlockUser();

    void unlockUser(Long empId);
}
