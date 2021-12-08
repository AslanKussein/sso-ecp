package kz.mtszn.models.repository;

import kz.mtszn.models.DepartmentUserContacts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentUserContactsRepository extends JpaRepository<DepartmentUserContacts, Long> {
}
