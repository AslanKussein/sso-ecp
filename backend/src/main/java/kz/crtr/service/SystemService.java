package kz.crtr.service;


import kz.crtr.dto.SystemDto;

import java.util.List;

public interface SystemService {
    List<SystemDto> getSystemList();

    List<SystemDto> getAll();

    SystemDto addSystem(final SystemDto dto);

    SystemDto editSystem(final Long id, final SystemDto dto);
}
