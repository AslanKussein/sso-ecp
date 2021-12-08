package kz.mtszn.models.repository;

import kz.mtszn.models.UerRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UerRole, Long> {

    Page<UerRole> findByEmpId(final Long empId, final Pageable pageable);
}
