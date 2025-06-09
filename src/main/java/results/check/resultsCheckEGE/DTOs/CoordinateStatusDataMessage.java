package results.check.resultsCheckEGE.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CoordinateStatusDataMessage {
    @JsonProperty("Status")
    private Status status;
    @JsonProperty("Documents")
    private Documents documents;
    @JsonProperty("ServiceNumber")
    private String serviceNumber;
}
