package kz.mtszn.service.impl;

import kz.mtszn.dto.UserDto;
import kz.mtszn.models.BlockUser;
import kz.mtszn.models.UserDetail;
import kz.mtszn.models.Users;
import kz.mtszn.models.repository.BlockUserRepository;
import kz.mtszn.models.repository.UsersRepository;
import kz.mtszn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final BlockUserRepository blockUserRepository;

    @Override
    public UserDto findUserByName(final String username) {
        final Users users = usersRepository.findByUsernameIgnoreCase(username);

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

    private String buildFullName(final UserDetail userDetail) {
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
}
