package kz.mtszn.service;

import kz.mtszn.dto.UserLogsDTO;
import kz.mtszn.dto.UserLogsRequestDTO;

public interface UserActionLogs {
    void createUserActionLogs(final UserLogsRequestDTO userLogsDTO);
    void createUserActionLogs(final UserLogsDTO dto);
}
