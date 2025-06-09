package results.check.resultsCheckEGE.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServiceDocument {
    @JsonProperty("CustomAttributes")
    private CustomAttributes customAttributes;
}
