package kz.mtszn.service.impl;

import kz.mtszn.dto.UserLogsDTO;
import kz.mtszn.dto.UserLogsRequestDTO;
import kz.mtszn.models.TSSOUserLogs;
import kz.mtszn.models.repository.TSSOUserLogsRepository;
import kz.mtszn.service.UserActionLogs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static kz.mtszn.util.Utils.getRequestRemoteAddress;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserActionLogsImpl implements UserActionLogs {

    private final TSSOUserLogsRepository tssoUserLogsRepository;

    @Override
    public void createUserActionLogs(final UserLogsRequestDTO dto) {
        TSSOUserLogs tssoUserLogs = new TSSOUserLogs();
        tssoUserLogs.setEventDate(dto.getEventDate());
        tssoUserLogs.setEmpId(dto.getEmpId());
        tssoUserLogs.setUsername(dto.getUsername());
        tssoUserLogs.setEventMessage(dto.getEventMessage());
        tssoUserLogs.setIp(getRequestRemoteAddress());

        tssoUserLogsRepository.save(tssoUserLogs);
    }

    @Override
    public void createUserActionLogs(final UserLogsDTO dto) {
        TSSOUserLogs tssoUserLogs = new TSSOUserLogs();
        tssoUserLogs.setEventDate(ZonedDateTime.now());
        tssoUserLogs.setEmpId(dto.getEmpId());
        tssoUserLogs.setUsername(dto.getUsername());
        tssoUserLogs.setAuthTypeId(dto.getAuthTypeId());
        tssoUserLogs.setEventMessage(dto.getEventMessage());
        tssoUserLogs.setIp(getRequestRemoteAddress());

        tssoUserLogsRepository.save(tssoUserLogs);
    }
}
