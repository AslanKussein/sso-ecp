package kz.crtr.service;


import kz.crtr.dto.SystemDto;

import java.util.List;

public interface SystemService {
    List<SystemDto> getSystemList();

    List<SystemDto> getAll();
}
