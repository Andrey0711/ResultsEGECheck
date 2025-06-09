package results.check.resultsCheckEGE.DTOs;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ExamResultList {
    private String id;
    private String registrationCode;
    private String documentNumber;
    private ArrayList<ExamResult> examResult;
}
