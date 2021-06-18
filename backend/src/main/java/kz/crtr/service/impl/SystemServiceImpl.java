package kz.crtr.service.impl;

import kz.crtr.dto.SystemDto;
import kz.crtr.models.Systems;
import kz.crtr.models.repository.SystemsRepository;
import kz.crtr.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    private final SystemsRepository systemsRepository;

    @Override
    public List<SystemDto> getSystemList() {
        return systemsRepository.findAll().stream().map(this::wrapper).collect(Collectors.toList());
    }

    @Override
    public List<SystemDto> getAll() {
        return systemsRepository.findAll().stream().map(this::wrapper).collect(Collectors.toList());
    }

    private SystemDto wrapper(final Systems systems) {
        return SystemDto.builder()
                .id(systems.getId())
                .name(systems.getName())
                .url(systems.getUrl())
                .urlEtc(systems.getUrl_etc())
                .build();
    }

    private Systems getSystemsById(final Long id) {
        return systemsRepository.getOne(id);
    }

    @Override
    public SystemDto addSystem(final SystemDto dto) {
        Systems systems = new Systems();
        systems.setName(dto.getName());
        systems.setUrl(dto.getUrl());
        systems.setUrl_etc(dto.getUrlEtc());
        return wrapper(systems);
    }

    @Transactional(readOnly=true)
    @Override
    public SystemDto editSystem(final Long id, final SystemDto dto) {
        Systems systems = getSystemsById(id);
        systems.setName(dto.getName());
        systems.setUrl(dto.getUrl());
        systems.setUrl_etc(dto.getUrlEtc());
        return wrapper(systemsRepository.save(systems));
    }
}
