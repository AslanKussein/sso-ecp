package kz.mtszn.service.impl;

import kz.mtszn.dto.*;
import kz.mtszn.exception.BadRequestException;
import kz.mtszn.exception.NotFoundException;
import kz.mtszn.models.*;
import kz.mtszn.models.repository.*;
import kz.mtszn.security.IAuthenticationFacade;
import kz.mtszn.service.UserService;
import kz.mtszn.util.BundleMessageUtil;
import kz.mtszn.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final BlockUserRepository blockUserRepository;
    private final PersonRepository personRepository;
    private final TSSOUserLogsRepository tssoUserLogsRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final IAuthenticationFacade authenticationFacade;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDto findUserByName(final String username) {
        final Users users = usersRepository.findByUsernameIgnoreCase(username);

        UserDto.UserDtoBuilder userDtoBuilder = UserDto.builder()
                .empId(users.getEmpId())
                .username(users.getUsername());
        if (nonNull(users.getUserDetail())) {
            userDtoBuilder
                    .phone(users.getUserDetail().getPhone())
                    .email(users.getUserDetail().getEmail())
                    .branch(users.getUserDetail().getBranch())
                    .fullName(buildFullName(users.getUserDetail()));
        }
        userDtoBuilder.availableSystems(getUserAvailableSystem(users.getEmpId()));
        userDtoBuilder.isAdmin(isAdmin(users.getEmpId()));

        getPersonInfo(userDtoBuilder, users.getUserDetail().getIin());

        return userDtoBuilder.build();
    }

    private void getPersonInfo(UserDto.UserDtoBuilder userDto, final String iin) {
        if (isNull(iin)) {
            return;
        }
        Person person = personRepository.findByRn(iin);
        if (nonNull(person)) {
            PersonDto build = PersonDto.builder()
                    .sicid(person.getSicid())
                    .address(person.getAddress())
                    .genderKz(person.getSex() == 1 ? BundleMessageUtil.getLocaledValue(LocalValue.kz, "gender.man") : BundleMessageUtil.getLocaledValue(LocalValue.kz, "gender.woman"))
                    .genderRu(person.getSex() == 1 ? BundleMessageUtil.getLocaledValue(LocalValue.ru, "gender.man") : BundleMessageUtil.getLocaledValue(LocalValue.ru, "gender.woman"))
                    .birthDate(DateUtils.formatHuman(person.getBirthdate()))
                    .build();

            userDto.personDto(build);
        }
    }

    private Boolean isAdmin(final Long empid) {
        Map<String, Object> map = new HashMap<>();
        map.put("empId", empid);
        StringJoiner sql = new StringJoiner(" ")
                .add("select id from websecurity.user_role where emp_Id=:empId and d_role_id=(select s.id from websecurity.d_role s where code like 'ROLE_ADMIN_SSO')");

        try {
            namedParameterJdbcTemplate.queryForObject(sql.toString(), map, String.class);
            return Boolean.TRUE;
        } catch (EmptyResultDataAccessException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public String buildFullName(final UserDetail userDetail) {
        return new StringJoiner(" ").add(userDetail.getFirstname()).add(userDetail.getLastname()).add(userDetail.getMiddlename()).toString();
    }

    private Set<String> getUserAvailableSystem(final Long empId) {

        return Set.of("em5");
    }

    @Override
    public List<BlockUser> getListBlockUser() {
        return blockUserRepository.findAllByFailurecounter(5L);
    }

    @Override
    public void unlockUser(final Long empId) {
        BlockUser blockUser = blockUserRepository.findByEmpId(empId);
        blockUserRepository.delete(blockUser);
    }

    @Override
    public PageDto<?> getUserAction(final Long empId, final PageableDTO pageable) {
        Page<TSSOUserLogs> page = tssoUserLogsRepository.findAllByEmpIdOrderByIdDesc(empId, PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

        List<UserLogsDTO> collect = page.getContent()
                .stream().map(e ->
                        UserLogsDTO.builder()
                                .id(e.getId())
                                .eventMessage(e.getEventMessage())
                                .eventDate(DateUtils.formatHumanDateTime(e.getEventDate()))
                                .ip(e.getIp())
                                .build()
                ).collect(Collectors.toList());

        return PageDto.builder()
                .content(collect)
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public void changePasswordDto(final LocalValue lang, final ChangePasswordDto changePasswordDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Users users = usersRepository.findByEmpIdAndPassword(authenticationFacade.getUser().getEmpId(), encoder.encode(changePasswordDto.getPassword()));
        if (isNull(users)) {
            throw NotFoundException.userNotFoundByPassword(lang);
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getReNewPassword())) {
            throw BadRequestException.passwordNewAndReNewNotSame(lang);
        }
        if (changePasswordDto.getNewPassword().length() < 8) {
            throw BadRequestException.passwordLengthLess8(lang);
        }
        if (!(Pattern.compile("[A-Z]").matcher(changePasswordDto.getNewPassword()).find() || Pattern.compile("[А-Я]").matcher(changePasswordDto.getNewPassword()).find())) {
            throw BadRequestException.passwordNotHasUpper(lang);
        }
        if (!Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]", Pattern.CASE_INSENSITIVE).matcher(changePasswordDto.getNewPassword()).find()) {
            throw BadRequestException.passwordNotHasSpecSymbols(lang);
        }

        users.setPassword(encoder.encode(changePasswordDto.getNewPassword()));
        users.setPassBeginDate(ZonedDateTime.now());
        users.setPassEndDate(ZonedDateTime.now().plusMonths(1));

        usersRepository.save(users);
    }

    @Override
    public PageDto<?> getUserRole(final Long empId, final PageableDTO pageable) {
        Page<UerRole> page = userRoleRepository.findByEmpId(empId, PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));

        return PageDto.builder()
                .content(page.getContent())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public BlockUserDto getCounter(final String userName) {
        BlockUserDto.BlockUserDtoBuilder builder = BlockUserDto.builder().failurecounter(1L);
        Optional<Users> users = usersRepository.findByUserNameIgnoreCaseAndBlockNotContaining(userName);
        if (users.isPresent()) {
            BlockUser blockUser = blockUserRepository.findByEmpId(users.get().getEmpId());
            if (nonNull(blockUser)) {
                builder.failurecounter(blockUser.getFailurecounter());
            }
        }
        return builder.build();
    }
}
