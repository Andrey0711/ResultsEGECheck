package results.check.resultsCheckEGE.DTOs;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ExamResult {
    private String isTraining;
    private String subjectName;
    private ArrayList<ResultPart> resultPart;
}
