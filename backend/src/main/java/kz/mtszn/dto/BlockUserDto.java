package kz.mtszn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BlockUserDto {
    private Long empId;
    private Long failurecounter;
    private String fullName;
    private String ip;
    private String blockDate;
}
