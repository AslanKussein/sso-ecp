package kz.crtr.service.impl;

import kz.crtr.dto.UserDto;
import kz.crtr.models.UserDetail;
import kz.crtr.models.Users;
import kz.crtr.models.repository.UsersRepository;
import kz.crtr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.StringJoiner;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    @Override
    public UserDto findUserByName(String username) {
        Users users = usersRepository.findByUsernameIgnoreCase(username);

        UserDto.UserDtoBuilder userDtoBuilder = UserDto.builder()
                .empId(users.getEmpId())
                .username(users.getUsername());
        if (nonNull(users.getUserDetail())) {
            userDtoBuilder
                    .branch(users.getUserDetail().getBranch())
                    .fullName(buildFullName(users.getUserDetail()));
        }
        userDtoBuilder.availableSystems(getUserAvailableSystem(users.getEmpId()));

        return userDtoBuilder.build();
    }

    private String buildFullName(UserDetail userDetail) {
        return new StringJoiner(" ").add(userDetail.getFirstname()).add(userDetail.getLastname()).add(userDetail.getMiddlename()).toString();
    }

    private Set<String> getUserAvailableSystem(Long empId) {

        return Set.of("em5");
    }
}
