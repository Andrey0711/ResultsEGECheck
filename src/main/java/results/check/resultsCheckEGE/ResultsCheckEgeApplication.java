package results.check.resultsCheckEGE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import results.check.resultsCheckEGE.Exceptions.ErrorOnClientSide;
import results.check.resultsCheckEGE.Exceptions.ErrorOnMosRuSide;

import java.net.URI;

@SpringBootApplication
public class ResultsCheckEgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResultsCheckEgeApplication.class, args);
	}

	@Bean
	public RestClient restClient(){
		return RestClient.builder()
				.requestFactory(getClientHttpRequestFactory())
				.defaultStatusHandler(HttpStatusCode::is4xxClientError,
						(request, response) -> {
							throw new ErrorOnMosRuSide("Ошибка на стороне MosRu");
						})
				.defaultStatusHandler(HttpStatusCode::is5xxServerError,
						((request, response) -> {
							throw new ErrorOnClientSide("Ошибка на стороне клиента");
						}))
				.build();
	}

	@Bean
	ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 5;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new
				HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectionRequestTimeout(timeout*1000);
		clientHttpRequestFactory.setConnectTimeout(timeout*2000);
		clientHttpRequestFactory.setReadTimeout(timeout*3000);
		return clientHttpRequestFactory;
	}
}
