package kz.mtszn.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "d_aiis", schema = "websecurity")
public class DAlias {
    @Id
    private Long id;
    private String code;
    @Column(name = "name_rus")
    private String nameRu;
    @Column(name = "name_kaz")
    private String nameKz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DAlias dAlias = (DAlias) o;
        return Objects.equals(id, dAlias.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
