package kz.mtszn.util;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class MapResultScanner implements IMapResultScanner {

    private final Map<String, Object> map;
    private boolean withCheck = true;

    public MapResultScanner(Map<String, Object> map) {
        this.map = map;
    }

    public MapResultScanner(Map<String, Object> map, boolean withCheck) {
        this.map = map;
        this.withCheck = withCheck;
    }

    @Override
    public String getString(String fieldName) {
        Object obj = getObjectOrThrow(fieldName);
        return obj == null ? null : obj.toString();
    }

    @Override
    public Integer getInteger(String fieldName) {
        Object obj = getObjectOrThrow(fieldName);
        return obj == null ? null : ((Number) obj).intValue();
    }

    @Override
    public Long getLong(String fieldName) {
        Object obj = getObjectOrThrow(fieldName);
        return obj == null ? null : ((Number) obj).longValue();
    }

    @Override
    public Boolean getBoolean(String fieldName) {
        return (Boolean) getObjectOrThrow(fieldName);
    }

    @Override
    public Double getDouble(String fieldName) {
        Object obj = getObjectOrThrow(fieldName);
        return obj == null ? null : ((Number) obj).doubleValue();
    }

    @Override
    public Date getDate(String fieldName) {
        return (Date) getObjectOrThrow(fieldName);
    }

    @Override
    public ZonedDateTime getZonedDateTime(String fieldName) {
        Date date = getDate(fieldName);
        return DateUtils.toZonedDateTime(date);
    }

    @Override
    public Object getObject(String fieldName) {
        return getObjectOrThrow(fieldName);
    }

    @Override
    public BigDecimal getBigDecimal(String fieldName) {
        return (BigDecimal) getObjectOrThrow(fieldName);
    }

    private Object getObjectOrThrow(String fieldName) {
        if (withCheck && !map.containsKey(fieldName)) {
            throw new IllegalArgumentException("Incorrect fieldName: " + fieldName);
        }
        return map.get(fieldName);
    }
}
