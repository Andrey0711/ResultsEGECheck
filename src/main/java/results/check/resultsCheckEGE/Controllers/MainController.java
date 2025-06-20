package results.check.resultsCheckEGE.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import results.check.resultsCheckEGE.DTOs.ExamResult;
import results.check.resultsCheckEGE.DTOs.MainResponseObj;
import results.check.resultsCheckEGE.Exceptions.ErrorWhileGetResultOfRequest;
import results.check.resultsCheckEGE.Exceptions.InvalidCookieExc;
import results.check.resultsCheckEGE.Services.SendRequestService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainController {

    private final SendRequestService requestService;

    @GetMapping("")
    public List<ExamResult> sendRequest(@RequestParam(value = "cookies", required = false) String cookies){
        if (cookies == null || cookies.isBlank() || cookies.length() < 100){
            throw new InvalidCookieExc("Cookie, отправленные вами, оказались неверны");
        }
        Optional<MainResponseObj> optionalMainResponseObj = requestService.sendMainRequestForGetData(cookies);
        validateMainResponse(optionalMainResponseObj);
        MainResponseObj mainResponseObj = optionalMainResponseObj.get();
        List<ExamResult> examResultList = mainResponseObj.getData().getCoordinateStatusDataMessage().getDocuments()
                .getServiceDocument().getCustomAttributes().getExamResultList().getExamResult();
         // проверка на isTraining, сортировка по этому
        return examResultList.stream().filter(e -> !Boolean.parseBoolean(e.getIsTraining()))
                .collect(Collectors.toList());
    }

    public void validateMainResponse(Optional<MainResponseObj> responseObj){
        if (responseObj.isEmpty()){
            throw new ErrorWhileGetResultOfRequest("Ошибка при получении запроса");
        }

        MainResponseObj mainResponseObj = responseObj.get();
        if (!mainResponseObj.getSuccess()){
            throw new ErrorWhileGetResultOfRequest("Статус получения результатов отрицательный");
        }
    }
}
