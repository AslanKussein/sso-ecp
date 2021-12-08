package kz.mtszn.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "person", schema = "solidary")
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "sicid")
    private Long sicid;
    private ZonedDateTime birthdate;
    private String address;
    private Integer sex;
    private String rn;


    @Override
    public String toString() {
        return "kz.mtszn.models.Person[ sicid=" + getSicid() + " ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Person person = (Person) o;
        return Objects.equals(sicid, person.sicid);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
