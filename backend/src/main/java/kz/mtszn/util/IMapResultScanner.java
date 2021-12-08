package kz.mtszn.util;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

public interface IMapResultScanner {
    String getString(String fieldName);

    Integer getInteger(String fieldName);

    Long getLong(String fieldName);

    Boolean getBoolean(String fieldName);

    Double getDouble(String fieldName);

    Date getDate(String fieldName);

    ZonedDateTime getZonedDateTime(String fieldName);

    Object getObject(String fieldName);

    BigDecimal getBigDecimal(String fieldName);
}
