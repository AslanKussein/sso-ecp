package kz.mtszn.service.impl;

import kz.mtszn.adapters.Adapters;
import kz.mtszn.dto.BlockUserDto;
import kz.mtszn.dto.SystemDto;
import kz.mtszn.dto.UserLogsDTO;
import kz.mtszn.models.BlockUser;
import kz.mtszn.models.DAlias;
import kz.mtszn.models.Systems;
import kz.mtszn.models.repository.BlockUserRepository;
import kz.mtszn.models.repository.DAliasRepository;
import kz.mtszn.models.repository.SystemsRepository;
import kz.mtszn.models.repository.UsersRepository;
import kz.mtszn.service.SystemService;
import kz.mtszn.service.UserService;
import kz.mtszn.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    private final SystemsRepository systemsRepository;
    private final UserActionLogsImpl userActionLogs;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final BlockUserRepository blockUserRepository;
    private final DAliasRepository dAliasRepository;
    private final UsersRepository usersRepository;
    private final UserService userService;

    @Override
    public List<SystemDto> getSystemList(final Long empId) {
        StringJoiner sql = new StringJoiner(" ")
                .add("select s.* from SOLIDARY.D_SSO_SYSTEMS s")
                .add("left join websecurity.v_d_aiis wd on wd.code = s.V_D_AIIS_CODE")
                .add("where wd.emp_id = :empId");

        try {
            return namedParameterJdbcTemplate.queryForList(sql.toString(), Map.of("empId", empId))
                    .stream()
                    .map(Adapters::toSystemDto)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return null;
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
                .aliasCode(systems.getAliasCode())
                .build();
    }

    @Override
    public SystemDto addSystem(final SystemDto dto) {
        Systems systems = new Systems();
        systems.setName(dto.getName());
        systems.setUrl(dto.getUrl());
        systems.setAliasCode(dto.getAliasCode());

        systems = systemsRepository.save(systems);

        return wrapper(systems);
    }

    @Override
    public SystemDto editSystem(final Long id, final SystemDto dto) {
        Systems systems = systemsRepository.getOne(id);
        systems.setName(dto.getName());
        systems.setUrl(dto.getUrl());
        systems.setAliasCode(dto.getAliasCode());
        systems = systemsRepository.save(systems);
        return wrapper(systems);
    }

    @Override
    public void remove(final Long id) {
        Systems systems = systemsRepository.getOne(id);
        systemsRepository.delete(systems);
    }

    @Override
    public void redirectSystem(final Long empId, final String userName, final SystemDto systemDto) {
        userActionLogs.createUserActionLogs(
                UserLogsDTO.builder()
                        .empId(empId)
                        .username(userName)
                        .eventMessage("Пользователь перешел в систему ".concat(systemDto.getName()))
                        .build()
        );
    }

    @Override
    public List<DAlias> getDAlias() {
        return dAliasRepository.findAll();
    }

    @Override
    public List<BlockUserDto> getBlockUserList() {
        return blockUserRepository.findAllByFailurecounter(5L).stream()
                .map(e ->
                        BlockUserDto.builder()
                                .empId(e.getEmpId())
                                .blockDate(DateUtils.formatHuman(e.getBlockDate()))
                                .ip(e.getIp())
                                .fullName(userService.buildFullName(usersRepository.getOne(e.getEmpId()).getUserDetail()))
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public void unBlockUser(final Long empId) {
        Optional<BlockUser> blockUser = blockUserRepository.findByEmpIdAndFailurecounterIn(empId, List.of(5L));
        blockUser.ifPresent(blockUserRepository::delete);
    }
}
