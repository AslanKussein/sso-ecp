package kz.mtszn.models.repository;

import kz.mtszn.models.BlockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockUserRepository extends JpaRepository<BlockUser, Long> {

    BlockUser findByEmpId(final Long empId);

    BlockUser findByEmpIdAndFailurecounter(final Long empId, final Long fCounter);

    List<BlockUser> findAllByFailurecounter(final Long fCounter);
}
