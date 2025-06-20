package results.check.resultsCheckEGE.DTOs.ForAppId;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppIdDTO {
    @JsonProperty("app_id")
    private Integer appId;
}
