package kz.mtszn.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "sso_user_logs", schema = "solidary")
public class TSSOUserLogs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "TSSOUserLogs_id_generator")
    @SequenceGenerator(name = "TSSOUserLogs_id_generator", sequenceName = "SOLIDARY.SEQ_SSO_USER_LOGS", allocationSize = 1)
    private Long id;
    private Long empId;
    private String username;
    private int authTypeId;
    private ZonedDateTime eventDate;
    private String eventMessage;
    private String ip;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TSSOUserLogs that = (TSSOUserLogs) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
