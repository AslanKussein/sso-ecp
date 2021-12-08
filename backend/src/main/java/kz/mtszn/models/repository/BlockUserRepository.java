package kz.mtszn.models.repository;

import kz.mtszn.models.BlockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockUserRepository extends JpaRepository<BlockUser, Long> {

    BlockUser findByEmpId(final Long empId);

    BlockUser findByEmpIdAndFailurecounter(final Long empId, final Long fCounter);

    Optional<BlockUser> findByEmpIdAndFailurecounterIn(final Long empId, final List<Long> fCounter);

    List<BlockUser> findAllByFailurecounter(final Long fCounter);

    void deleteByEmpId(final Long empId);
}
