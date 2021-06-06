package kz.crtr.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "branch", schema = "websecurity")
public class DBranch implements Serializable {
    @Id
    private String id;
    private String nameRus;
    private String nameKaz;
}
