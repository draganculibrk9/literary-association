package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.dtos.BetaReaderDTO;
import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ChooseBetaReadersDelegate implements JavaDelegate {

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");
        Set<BetaReaderDTO> betaReaders = UtilService.parseBetaReaders((String) data.get("beta_readers"));
        Set<String> betaReaderUsernames = new HashSet<>();
        betaReaders.forEach(reader -> betaReaderUsernames.add(reader.getBeta_readers()));
        delegateExecution.setVariable("beta_readers", new ArrayList<>(betaReaderUsernames));

        notificationService.sendSuccessNotification("Beta readers successfully chosen");
    }
}
