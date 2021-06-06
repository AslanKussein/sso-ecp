package kz.crtr.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Data
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
    @Column(name = "u_password")
    private String password;
    private Integer block;
    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserDetail userDetail;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getEmpId() != null ? getEmpId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        return (this.getEmpId() != null || other.getEmpId() == null) && (this.getEmpId() == null || this.empId.equals(other.empId));
    }

    @Override
    public String toString() {
        return "kz.crtr.models.Users[ id=" + getEmpId() + " ]";
    }
}
