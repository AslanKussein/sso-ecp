package kz.crtr.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EDSInfoGson {

    private String certXml;
    private String cn;
    private String sn;
    private String givenName;
    private String iin;
    private Date dateBegin;
    private Date dateTo;
    private Boolean valid;
    private Boolean fiz;
}
