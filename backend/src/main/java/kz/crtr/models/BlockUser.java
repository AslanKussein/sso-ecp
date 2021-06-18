package kz.crtr.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "block_user", schema = "solidary")
public class BlockUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long empId;
    private Long failureCounter;
}
