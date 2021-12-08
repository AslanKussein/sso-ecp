package kz.mtszn.models.repository;

import kz.mtszn.models.TSSOUserLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TSSOUserLogsRepository extends JpaRepository<TSSOUserLogs, Long> {

    Page<TSSOUserLogs> findAllByEmpIdOrderByIdDesc(final Long empId, final Pageable pageable);
}
