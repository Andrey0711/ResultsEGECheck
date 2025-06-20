package results.check.resultsCheckEGE.Services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import results.check.resultsCheckEGE.DTOs.ForAppId.AppIdDTO;
import results.check.resultsCheckEGE.DTOs.MainResponseObj;
import results.check.resultsCheckEGE.Exceptions.ErrorWhileGetAppIdExc;
import results.check.resultsCheckEGE.Exceptions.ErrorWhileGetUniqueHashExc;
import results.check.resultsCheckEGE.Utils.ParseTextUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
@RequiredArgsConstructor
public class SendRequestService {

    private final RestClient restClient;
    @Value("${mos.ru.request.url}")
    private String MAIN_REQUEST_URL_FOR_GET_DATA;
    @Value("${mos.ru.request.url.for.get.uniquehash}")
    private String REQUEST_FOR_GET_HASH_URL;
    @Value("${mos.ru.request.url.for.get.appid}")
    private String REQUEST_FOR_GET_APPID;
    @Value("${mos.ru.regex.for.parse.first.request}")
    private String REGEX_FOR_FIRST_REQUEST_HASH;


    public Optional<MainResponseObj> sendMainRequestForGetData(String requiredCookieForMosRuAuth) {
        Integer uniqueAppId = requestForGetAppId(requiredCookieForMosRuAuth);
        ResponseEntity<MainResponseObj> request = restClient.post()
                .uri(MAIN_REQUEST_URL_FOR_GET_DATA.formatted(
                        String.valueOf(uniqueAppId)
                ))
                .contentType(APPLICATION_FORM_URLENCODED)
                .acceptCharset(UTF_8)
                .header("Cookie", requiredCookieForMosRuAuth)
                .retrieve().toEntity(MainResponseObj.class);
        return Optional.ofNullable(request.getBody());

    }

    public Integer requestForGetAppId(String requiredCookieForMosRuAuth) {
        String uniqueHash = requestForGetUniqueHash(requiredCookieForMosRuAuth);
        AppIdDTO body = restClient.post()
                .uri(REQUEST_FOR_GET_APPID.formatted(uniqueHash))
                .contentType(APPLICATION_FORM_URLENCODED)
                .header("Cookie", requiredCookieForMosRuAuth)
                .acceptCharset(UTF_8)
                .retrieve()
                .body(AppIdDTO.class);
        if (body == null || body.getAppId() == null || body.getAppId() <= 350_000_000){
            throw new ErrorWhileGetAppIdExc("Ошибка при получении уникального id приложения для последующих запросов");
        }
        return body.getAppId();
    }

    @SneakyThrows
    public String requestForGetUniqueHash(String requiredCookieForMosRuAuth) {
        ResponseEntity<Void> body = restClient.get()
                .uri(REQUEST_FOR_GET_HASH_URL)
                .header("Cookie", requiredCookieForMosRuAuth)
                .retrieve()
                .toBodilessEntity();
        String text = null;
        if (body.getStatusCode().is3xxRedirection()){
            InputStream inputStream = null;
            try  {
                do {
                    inputStream = attemptWhileCantGetStatusOK(requiredCookieForMosRuAuth,
                            body.getHeaders().getLocation());
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } while (inputStream == null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            text = new String(inputStream.readAllBytes());
        }

        if(text == null || text.isBlank()) {
            throw new ErrorWhileGetUniqueHashExc("Ошибка при получении уникального хэша для последующих запросов");
        }
        return ParseTextUtil.parse(text, REGEX_FOR_FIRST_REQUEST_HASH);
    }

    @SneakyThrows
    public InputStream attemptWhileCantGetStatusOK(String requiredCookieForMosRuAuth, URI location){
        return restClient.get()
                .uri(location)
                .header("Cookie", requiredCookieForMosRuAuth)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is3xxRedirection()){
                        return null;
                    }
                    return response.getBody();
                });
    }

}
