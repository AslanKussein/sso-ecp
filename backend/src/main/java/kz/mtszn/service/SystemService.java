package kz.mtszn.service;


import kz.mtszn.dto.BlockUserDto;
import kz.mtszn.dto.SystemDto;
import kz.mtszn.models.DAlias;

import java.util.List;

public interface SystemService {
    List<SystemDto> getSystemList(final Long empId);

    List<SystemDto> getAll();

    SystemDto addSystem(final SystemDto dto);

    SystemDto editSystem(final Long id, final SystemDto dto);

    void redirectSystem(final Long empId, final String userName, final SystemDto systemDto);

    List<DAlias> getDAlias();

    List<BlockUserDto> getBlockUserList();

    void unBlockUser(final Long unBlockUser);
}
