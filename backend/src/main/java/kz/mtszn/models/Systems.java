package kz.mtszn.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "D_SSO_SYSTEMS", schema = "SOLIDARY")
public class Systems implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "Systems_id_generator")
    @SequenceGenerator(name = "Systems_id_generator", sequenceName = "SOLIDARY.SEQ_D_SSO_SYSTEMS", allocationSize = 1)
    private Long id;
    @Column(name = "system_name")
    private String name;
    private String url;
    private String url_etc;
    @Column(name = "V_D_AIIS_CODE")
    private String aliasCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Systems systems = (Systems) o;
        return Objects.equals(id, systems.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
