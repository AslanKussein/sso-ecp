package kz.mtszn.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "D_SSO_SYSTEMS", schema = "SOLIDARY")
public class Systems implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @Column(name = "system_name")
    private String name;
    private String url;
    private String url_etc;
}