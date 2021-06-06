package kz.crtr.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode()
@ToString
@Data
@Entity
@Table(name = "USER_DETAIL", schema = "websecurity")
public class UserDetail implements Serializable {
    @Id
    private String id;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private Users user;
    private String iin;
    private String lastname;
    private String firstname;
    private String middlename;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private DBranch branch;
}
