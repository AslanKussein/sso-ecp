package kz.mtszn.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "department_user_contacts", schema = "solidary")
public class DepartmentUserContacts implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String nameRu;
    private String nameKz;
    private String contactRu;
    private String contactKz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DepartmentUserContacts that = (DepartmentUserContacts) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
