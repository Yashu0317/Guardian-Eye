package snpsu.mca.javaFS.Guardian.Eye.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private String status;
    private String message;
    private Object data;
    
    public static ResponseDTO success(String message, Object data) {
        return new ResponseDTO("SUCCESS", message, data);
    }
    
    public static ResponseDTO error(String message) {
        return new ResponseDTO("ERROR", message, null);
    }
    
    public static ResponseDTO success(String message) {
        return new ResponseDTO("SUCCESS", message, null);
    }
}