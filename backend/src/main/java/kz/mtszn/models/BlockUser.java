package kz.mtszn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "block_user", schema = "solidary")
public class BlockUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long empId;
    private Long failurecounter;
    private Instant blockDate;
}
