package kz.mtszn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogsDTO {
    private Long empId;
    private Long id;
    private String username;
    private int authTypeId;
    private String eventMessage;
    private String eventDate;
    private String ip;
}
