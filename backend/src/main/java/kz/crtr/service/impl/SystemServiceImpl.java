package kz.crtr.service.impl;

import kz.crtr.dto.SystemDto;
import kz.crtr.models.repository.SystemsRepository;
import kz.crtr.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    private final SystemsRepository systemsRepository;

    @Override
    public List<SystemDto> getSystemList() {
        return systemsRepository.findAll().stream().map(
                systems -> SystemDto.builder()
                        .id(systems.getId())
                        .name(systems.getName())
                        .url(systems.getUrl())
                        .urlEtc(systems.getUrl_etc())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<SystemDto> getAll() {
        return systemsRepository.findAll().stream().map(
                systems -> SystemDto.builder()
                        .id(systems.getId())
                        .name(systems.getName())
                        .url(systems.getUrl())
                        .urlEtc(systems.getUrl_etc())
                        .build()
        ).collect(Collectors.toList());
    }
}
