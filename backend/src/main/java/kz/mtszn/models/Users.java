package kz.mtszn.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "websecurity")
public class Users implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EMP_ID")
    private Long empId;
    @Column(name = "u_name")
    private String username;
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "u_password")
    @ToString.Exclude
    private String password;
    private Integer block;
    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserDetail userDetail;
    @Column(name = "PASS_BEGIN_DATE")
    private ZonedDateTime passBeginDate;
    @Column(name = "PASS_END_DATE")
    private ZonedDateTime passEndDate;

    @Override
    public String toString() {
        return "kz.mtszn.models.Users[ id=" + getEmpId() + " ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Users users = (Users) o;
        return Objects.equals(empId, users.empId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
