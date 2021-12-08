package kz.mtszn.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "block_user", schema = "solidary")
public class BlockUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long empId;
    private Long failurecounter;
    private ZonedDateTime blockDate;
    private String ip;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BlockUser blockUser = (BlockUser) o;
        return Objects.equals(empId, blockUser.empId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
