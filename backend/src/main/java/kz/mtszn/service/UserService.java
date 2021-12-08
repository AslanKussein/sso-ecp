package kz.mtszn.service;

import kz.mtszn.dto.*;
import kz.mtszn.models.BlockUser;
import kz.mtszn.models.UserDetail;

import java.util.List;

public interface UserService {
    UserDto findUserByName(final String username);

    String buildFullName(UserDetail userDetail);

    List<BlockUser> getListBlockUser();

    void unlockUser(Long empId);

    PageDto<?> getUserAction(final Long empId, final PageableDTO pageable);

    void changePasswordDto(LocalValue lang, ChangePasswordDto changePasswordDto);

    PageDto<?> getUserRole(final Long empId, final PageableDTO pageable);

    BlockUserDto getCounter(String userName);
}
