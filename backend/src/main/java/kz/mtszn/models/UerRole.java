package kz.mtszn.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "v_user_d_role", schema = "websecurity")
public class UerRole {
    @Id
    private Long id;
    private Long empId;
    @Column(name = "name_rus")
    private String nameRu;
    @Column(name = "name_kaz")
    private String nameKz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UerRole uerRole = (UerRole) o;
        return Objects.equals(id, uerRole.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
