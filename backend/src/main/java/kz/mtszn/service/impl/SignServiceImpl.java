package kz.mtszn.service.impl;

import io.jsonwebtoken.Claims;
import kz.mtszn.controller.token.LoginRequestDto;
import kz.mtszn.controller.token.RefreshTokenRequestDto;
import kz.mtszn.controller.token.TokenRequestDto;
import kz.mtszn.dto.*;
import kz.mtszn.exception.BadRequestException;
import kz.mtszn.exception.NotFoundException;
import kz.mtszn.models.BlockUser;
import kz.mtszn.models.Users;
import kz.mtszn.models.redis.AuthorizeUser;
import kz.mtszn.models.redis.BlackListUser;
import kz.mtszn.models.redisrepository.AuthorizeUserRepository;
import kz.mtszn.models.redisrepository.BlackListUserRepository;
import kz.mtszn.models.repository.BlockUserRepository;
import kz.mtszn.models.repository.UsersRepository;
import kz.mtszn.security.CurrentUser;
import kz.mtszn.security.UserDetailsServiceImpl;
import kz.mtszn.service.SignService;
import kz.mtszn.service.SystemService;
import kz.mtszn.service.UserActionLogs;
import kz.mtszn.util.EDSUtil;
import kz.mtszn.util.JwtTokenUtil;
import kz.mtszn.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static kz.mtszn.util.Utils.getRequestRemoteAddress;

@RequiredArgsConstructor
@Service
public class SignServiceImpl implements SignService {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;
    private final UsersRepository usersRepository;
    private final BlockUserRepository blockUserRepository;
    private final UserActionLogs userActionLogs;
    private final AuthorizeUserRepository authorizeUserRepository;
    private final BlackListUserRepository blackListUserRepository;
    private final SystemService systemService;

    private void checkUserHasAccessSSO(final Long empId, final LocalValue language) {
        List<SystemDto> systemList = systemService.getSystemList(empId);
        if (systemList.isEmpty()) {
            throw BadRequestException.userHasNotAccessSSO(language);
        }
    }

    @Override
    public UserTokenState sign(final LocalValue language, final LoginRequestDto loginRequest) {
        Authentication authentication = null;
        final Users users;
        if (StringUtils.isNotEmpty(loginRequest.getCertificate())) {
            users = getUserByEds(loginRequest.getCertificate(), language);

            if (isNull(users)) {
                throw BadRequestException.notCorrectUserException(language);
            }

            checkUserHasAccessSSO(users.getEmpId(), language);

            UserDetails userDetails = getUserDetails(language, users.getUsername());

            if (nonNull(userDetails)) {
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                setUserLogs(users.getEmpId(), users.getUsername(), 1, "Вход с помощью ЭЦП");
            }
        } else {
            final UserDetails userDetail = getUserDetails(language, loginRequest.getUsername());

            users = usersRepository.findByUsernameIgnoreCase(userDetail.getUsername());

            checkUserHasAccessSSO(users.getEmpId(), language);

            checkFailureCounterBlockUser(users.getEmpId(), language);

            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), userDetail.getAuthorities())
                );
                setUserLogs(users.getEmpId(), users.getUsername(), 2, "Вход с помощью ЛОГИН/ПАРОЛЬ");
                checkAndRemoveUser(users.getEmpId());
            } catch (AuthenticationException e) {
                checkAndBlockUser(users.getEmpId(), language);
                setUserLogs(users.getEmpId(), users.getUsername(), 2, "Не удачная попытка Входа с помощью ЛОГИН/ПАРОЛЬ");
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

        UserTokenState userTokenState = tokenUtil.generateToken(currentUser.getUser().getEmpId().toString(), currentUser.getUser().getUsername());

        createAndRemoveOldTokenDate(users.getEmpId(), userTokenState);

        return userTokenState;
    }

    private void checkFailureCounterBlockUser(final Long empId, final LocalValue language) {
        BlockUser blockUser = blockUserRepository.findByEmpIdAndFailurecounter(empId, 5L);
        if (nonNull(blockUser)) {
            throw BadRequestException.userBlock(language);
        }
    }

    private void checkAndRemoveUser(final Long empId) {
        Optional<BlockUser> blockUser = blockUserRepository.findByEmpIdAndFailurecounterIn(empId, List.of(1L, 2L, 3L, 4L));
        blockUser.ifPresent(blockUserRepository::delete);
    }


    private void checkAndBlockUser(final Long empId, final LocalValue language) {
        BlockUser blockUser = blockUserRepository.findByEmpId(empId);
        if (nonNull(blockUser)) {
            blockUser.setFailurecounter(blockUser.getFailurecounter() + 1);

        } else {
            blockUser = new BlockUser();
            blockUser.setFailurecounter(1L);
            blockUser.setEmpId(empId);
            blockUser.setIp(getRequestRemoteAddress());
        }
        blockUser.setBlockDate(ZonedDateTime.now());
        blockUserRepository.save(blockUser);
        throw BadRequestException.notCorrectUserException(language);
    }

    private UserDetails getUserDetails(final LocalValue language, final String username) {
        try {
            return loadUserByUsername(language, username);
        } catch (Exception e) {
            throw BadRequestException.notCorrectUserException(language);
        }
    }

    private UserDetails loadUserByUsername(final LocalValue localValue, final String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (isNull(userDetails)) {
            throw NotFoundException.userNotFound(localValue, username);
        }
        return userDetails;
    }

    private Users getUserByEds(final String certificate, final LocalValue language) {
        EDSInfoGson edsInfo = null;
        try {
            EDSUtil edsUtil = new EDSUtil();
            edsInfo = edsUtil.getInfo(certificate);

            if (isNull(edsInfo)) {
                throw BadRequestException.getEcpError(language);
            }

            if (isNull(edsInfo.getIin())) {
                throw BadRequestException.getEcpErrorIin(language);
            }

            if (!edsInfo.getValid()) {
                throw BadRequestException.getEcpErrorValid(language);
            }

            return usersRepository.findByUserDetail_IinAndBlockNot(edsInfo.getIin());
        } catch (Exception e) {
            setUserLogs(null, nonNull(edsInfo) ? edsInfo.getIin() : null, 1, "Не удачная попытка Входа с помощью ЭЦП");
            throw BadRequestException.notCorrectUserException(language);
        }
    }

    @Override
    public UserTokenState refreshToken(final RefreshTokenRequestDto dto, final LocalValue language) throws Exception {
        if (StringUtils.isEmpty(dto.getRefreshToken())) {
            throw BadRequestException.emptyRefreshToken(language);
        }
        Optional<BlackListUser> blackListUser = blackListUserRepository.findByAccessToken(dto.getAccessToken());
        if (blackListUser.isPresent()) {
            throw new Exception();
        }

        Claims claims = tokenUtil.getAllClaimsFromToken(dto.getRefreshToken());

        return tokenUtil.generateToken(claims.getId(), claims.getSubject());
    }

    @Override
    public UserTokenState validateToken(final TokenRequestDto dto) {
        return tokenUtil.getUserTokenState(dto.getToken());
    }

    private void setUserLogs(final Long empId, final String username, final int authTypeId, final String message) {
        userActionLogs.createUserActionLogs(
                UserLogsDTO.builder()
                        .empId(empId)
                        .username(username)
                        .authTypeId(authTypeId)
                        .eventMessage(message)
                        .build()
        );
    }

    private void createAndRemoveOldTokenDate(final Long empId, final UserTokenState userTokenState) {
//        authorizeUserRepository.deleteAll();
        authorizeUserRepository.findAll().forEach(e -> System.out.println(e.getId()));
        Optional<AuthorizeUser> authorizeUser = authorizeUserRepository.findById(String.valueOf(empId));
        if (authorizeUser.isPresent()) {
            BlackListUser blackListUser = BlackListUser.builder().accessToken(authorizeUser.get().getAccessToken()).build();
            blackListUserRepository.save(blackListUser);

            authorizeUserRepository.deleteById(authorizeUser.get().getId());
        }
        AuthorizeUser authorize = AuthorizeUser.builder()
                .id(String.valueOf(empId))
                .accessToken(userTokenState.getAccessToken())
                .crDate(ZonedDateTime.now())
                .build();
        authorizeUserRepository.save(authorize);
    }
}
