package kz.mtszn.models.repository;

import kz.mtszn.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByRn(final String iin);
}
