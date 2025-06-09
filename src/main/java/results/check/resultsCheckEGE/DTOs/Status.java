package results.check.resultsCheckEGE.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Status{
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("StatusTitle")
    private String statusTitle;
    @JsonProperty("StatusDate")
    private String statusDate;
}

