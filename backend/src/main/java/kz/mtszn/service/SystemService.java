package kz.mtszn.service;


import kz.mtszn.dto.SystemDto;

import java.util.List;

public interface SystemService {
    List<SystemDto> getSystemList();

    List<SystemDto> getAll();

    SystemDto addSystem(final SystemDto dto);

    SystemDto editSystem(final Long id, final SystemDto dto);
}
