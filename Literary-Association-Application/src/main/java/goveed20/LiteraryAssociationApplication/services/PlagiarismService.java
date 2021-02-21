package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.PlagiarismResultDTO;
import goveed20.LiteraryAssociationApplication.utils.PlagiatorProxy;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class PlagiarismService {
    @Autowired
    private PlagiatorProxy plagiatorProxy;

    public PlagiarismResultDTO uploadPaper(String title, String path, Boolean isNew) throws IOException {
        MultipartFile file = new MockMultipartFile(title, title + ".pdf", "text/plain",
                IOUtils.toByteArray(new FileInputStream(path)));

        return isNew ? plagiatorProxy.uploadNewPaper(file) : plagiatorProxy.uploadExistingPaper(file);
    }

    public void deletePaper(Long id) {
        try {
            plagiatorProxy.deletePaper(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
