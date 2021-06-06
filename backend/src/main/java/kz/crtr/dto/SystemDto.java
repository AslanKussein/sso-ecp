package kz.crtr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemDto implements Serializable {
    private Long id;
    private String name;
    private String url;
    private String urlEtc;
}
