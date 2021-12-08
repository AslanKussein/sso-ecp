package kz.mtszn.adapters;

import kz.mtszn.dto.SystemDto;
import kz.mtszn.util.MapResultScanner;

import java.util.Map;

public class Adapters {

    public static SystemDto toSystemDto(Map<String, Object> map) {
        MapResultScanner scanner = new MapResultScanner(map);

        return SystemDto.builder()
                .id(scanner.getLong("ID"))
                .name(scanner.getString("SYSTEM_NAME"))
                .url(scanner.getString("URL"))
                .urlEtc(scanner.getString("URL_ETC"))
                .build();
    }


}
