package goveed20.LiteraryAssociationApplication.elasticsearch.services;

import goveed20.LiteraryAssociationApplication.elasticsearch.repositories.BetaReaderIndexingUnitRepository;
import goveed20.LiteraryAssociationApplication.elasticsearch.repositories.BookIndexingUnitRepository;
import goveed20.LiteraryAssociationApplication.elasticsearch.units.BetaReaderIndexingUnit;
import goveed20.LiteraryAssociationApplication.elasticsearch.units.BookIndexingUnit;
import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.Reader;
import goveed20.LiteraryAssociationApplication.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.stream.Collectors;

@Service
public class IndexingService {
    @Autowired
    private PdfService pdfService;

    @Autowired
    private BetaReaderIndexingUnitRepository betaReaderIndexingUnitRepository;

    @Autowired
    private BookIndexingUnitRepository bookIndexingUnitRepository;

    public void indexBook(Book book) {
        bookIndexingUnitRepository.save(
                BookIndexingUnit.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .genre(book.getGenre().getGenre().getSerbianName())
                        .openAccess(book.getPrice() == 0.0)
                        .writers(getAuthors(book))
                        .basicInfo(createBasicInfo(book))
                        .text(pdfService.extractText(new File(book.getFile())))
                        .build()
        );
    }

    private String createBasicInfo(Book book) {
        return String.format("%s - %s, %d - %s", getAuthors(book), book.getPublisher(), book.getPublicationYear(), book.getPublicationPlace());
    }

    private String getAuthors(Book book) {
        return String.format("%s %s, %s", book.getWriter().getName(), book.getWriter().getSurname(), book.getAdditionalAuthors());
    }

    public void indexBetaReader(Reader reader) {
        betaReaderIndexingUnitRepository.save(
                BetaReaderIndexingUnit.builder()
                        .id(reader.getId())
                        .location(new GeoPoint(reader.getLocation().getLatitude(), reader.getLocation().getLongitude()))
                        .name(String.format("%s %s", reader.getName(), reader.getSurname()))
                        .genres(reader.getBetaReaderStatus().getBetaGenres().stream()
                                .map(g -> g.getGenre().getSerbianName())
                                .collect(Collectors.toList()))
                        .username(reader.getUsername())
                        .build()
        );
    }

    public void deleteAllIndexes() {
        bookIndexingUnitRepository.deleteAll();
        betaReaderIndexingUnitRepository.deleteAll();
    }
}
