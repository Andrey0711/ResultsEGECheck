package results.check.resultsCheckEGE.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import results.check.resultsCheckEGE.DTOs.ExamResult;
import results.check.resultsCheckEGE.DTOs.MainResponseObj;
import results.check.resultsCheckEGE.Exceptions.ErrorWhileGetResultOfRequest;
import results.check.resultsCheckEGE.Services.SendRequestService;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainController {

    private final SendRequestService requestService;

    @PostMapping("")
    public List<ExamResult> sendRequest(){
        Optional<MainResponseObj> responseObj = requestService.sendRequest();
        if (responseObj.isEmpty()){
            throw new ErrorWhileGetResultOfRequest("Ошибка при получении запроса");
        }

        MainResponseObj mainResponseObj = responseObj.get();
        if (!mainResponseObj.getSuccess()){
            throw new ErrorWhileGetResultOfRequest("Статус получения результатов отрицательный");
        }

        List<ExamResult> examResultList = mainResponseObj.getData().getCoordinateStatusDataMessage().getDocuments()
                .getServiceDocument().getCustomAttributes().getExamResultList().getExamResult();
        System.out.println(examResultList.size()); // проверка на isTraining, сортировка по этому
        return examResultList;
    }
}
