package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.dtos.PaperDTO;
import goveed20.LiteraryAssociationApplication.dtos.PlagiarismResultDTO;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.services.PlagiarismService;
import lombok.SneakyThrows;
import one.util.streamex.StreamEx;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UploadToPlagiarismSystemDelegate implements JavaDelegate {
    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private PlagiarismService plagiarismService;

    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String title = String.valueOf(delegateExecution.getVariable("working_paper"));
        WorkingPaper workingPaper = workingPaperRepository.findByTitle(title);

        try {
            PlagiarismResultDTO results = plagiarismService.uploadPaper(workingPaper.getTitle(), workingPaper.getFile(), true);
            assert results != null;

            delegateExecution.setVariable("uploaded_paper_id", results.getUploadedPaper().getId());

            List<String> titlesAndPercents = StreamEx.of(results.getSimilarPapers())
                    .distinct(PaperDTO::getTitle)
                    .filter(p -> !p.getTitle().contains(title))
                    .map(p -> String.format("%s %f%%", p.getTitle(), p.getSimilarProcent()))
                    .collect(Collectors.toList());

            delegateExecution.setVariable("similar_papers", titlesAndPercents);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            delegateExecution.setVariable("similar_papers", new ArrayList<>());
        }
    }
}
