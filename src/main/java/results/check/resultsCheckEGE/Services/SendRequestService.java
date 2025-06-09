package results.check.resultsCheckEGE.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import results.check.resultsCheckEGE.DTOs.MainResponseObj;

import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
@RequiredArgsConstructor
public class SendRequestService {

    private final RestClient restClient;
    @Value("${mos.ru.request.url}")
    private String REQUEST_URL;
    @Value("${mos.ru.required.header}")
    private String REQUIRED_HEADERS;
    @Value("${mos.ru.request.url.addiction.params}")
    private String REQUIRED_ADDICTION_PARAMS;

    public Optional<MainResponseObj> sendRequest(){
         ResponseEntity<MainResponseObj> request = restClient.post()
                .uri(REQUEST_URL + REQUIRED_ADDICTION_PARAMS)
                .contentType(APPLICATION_FORM_URLENCODED)
                .acceptCharset(UTF_8)
                .header("Cookie", REQUIRED_HEADERS)
                .retrieve().toEntity(MainResponseObj.class);
        return Optional.ofNullable(request.getBody());
    }
}
