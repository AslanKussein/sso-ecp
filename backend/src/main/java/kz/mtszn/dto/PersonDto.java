package kz.mtszn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long sicid;
    private String iin;
    private String genderKz;
    private String genderRu;
    private String birthDate;
    private String address;
}
