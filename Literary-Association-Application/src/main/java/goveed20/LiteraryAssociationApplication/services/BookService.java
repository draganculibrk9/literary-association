package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.BookDTO;
import goveed20.LiteraryAssociationApplication.dtos.BookListItemDTO;
import goveed20.LiteraryAssociationApplication.dtos.SearchQueryDTO;
import goveed20.LiteraryAssociationApplication.elasticsearch.units.BookIndexingUnit;
import goveed20.LiteraryAssociationApplication.elasticsearch.utils.BooleanOperator;
import goveed20.LiteraryAssociationApplication.elasticsearch.utils.ResultMapper;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.Reader;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final String booksFolder = "literary-association/Literary-Association-Application/src/main/resources/books/";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GenreRepository genreRepository;

    public Page<BookIndexingUnit> searchBooks(SearchQueryDTO searchQuery) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        searchQuery.getSearchParams().forEach(sp -> {
            String key = sp.getKey();
            String value = sp.getValue();

            if (sp.getBooleanOperator().equals(BooleanOperator.AND)) {
                if (sp.getPhraze()) {
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(key, value));
                } else {
                    boolQueryBuilder.must(QueryBuilders.commonTermsQuery(key, value));
                }
            } else {
                if (sp.getPhraze()) {
                    boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(key, value));
                } else {
                    boolQueryBuilder.should(QueryBuilders.commonTermsQuery(key, value));
                }
            }
        });

        Reader reader = (goveed20.LiteraryAssociationApplication.model.Reader)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        getBookTitles(reader).forEach(title -> {
            boolQueryBuilder.mustNot(QueryBuilders.commonTermsQuery("title", title));
        });

        SearchQuery query = queryBuilder.withQuery(boolQueryBuilder).withHighlightFields(
                new HighlightBuilder.Field("text")
                        .preTags("<b>")
                        .postTags("</b>")
                        .numOfFragments(1)
                        .fragmentSize(250)
        ).withPageable(PageRequest.of(searchQuery.getPage(), 3))
                .build();

        return elasticsearchTemplate.queryForPage(query, BookIndexingUnit.class, new ResultMapper());
    }

    public List<BookListItemDTO> getBooks() {
        Set<String> bookTitles = new HashSet<>();

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(UserRole.READER)) {
            goveed20.LiteraryAssociationApplication.model.Reader reader = (goveed20.LiteraryAssociationApplication.model.Reader)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            bookTitles = getBookTitles(reader);
        }

        return bookRepository.findByTitleNotIn(bookTitles)
                .stream()
                .map(b -> new BookListItemDTO(b.getId(), b.getTitle(), b.getPublisher(), b.getISBN(), b
                        .getPublicationYear()))
                .collect(Collectors.toList());
    }

    public BookDTO getBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isEmpty()) {
            throw new NotFoundException(String.format("Book with id '%d' not found", id));
        }

        Book book = bookOptional.get();

        return BookDTO.builder()
                .id(book.getId())
                .genreEnum(book.getGenre().getGenre())
                .ISBN(book.getISBN())
                .place(book.getPublicationPlace())
                .publisher(book.getPublisher())
                .synopsis(book.getSynopsis())
                .title(book.getTitle())
                .price(book.getPrice())
                .year(book.getPublicationYear())
                .build();
    }


    public WorkingPaper submitPaper(String processID, String path) throws IOException {
        File writingsFile = new File(path);
        String writingsString = FileUtils.readFileToString(writingsFile);
        FileUtils.forceDelete(writingsFile);
        if (!writingsString.contains("data:application/pdf;base64,")) {
            throw new BusinessProcessException("Invalid file type. It should be a PDF file");
        }
        writingsString = writingsString.replace("data:application/pdf;base64,", "");
        byte[] decoded = Base64.getMimeDecoder().decode(writingsString.getBytes(StandardCharsets.UTF_8));

        String workingPaperTitle = (String) runtimeService.getVariable(processID, "working_paper");
        WorkingPaper paper = workingPaperRepository.findByTitle(workingPaperTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Working paper is not found");
        }

        File filePaper = new File(booksFolder + workingPaperTitle + ".pdf");
        filePaper.createNewFile();
        OutputStream os = new FileOutputStream(filePaper);
        os.write(decoded);
        os.flush();
        os.close();

        paper.setFile(filePaper.getPath());
        return paper;
    }

    public ResponseEntity downloadBook(String bookTitle) throws Exception {
        WorkingPaper paper = workingPaperRepository.findByTitle(bookTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Book with given title does not exist");
        }

        File file = new File(paper.getFile());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public List<BookListItemDTO> getMyBooks() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(UserRole.WRITER)) {
            goveed20.LiteraryAssociationApplication.model.Writer writer = (goveed20.LiteraryAssociationApplication.model.Writer)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return writer.getBooks().stream()
                    .map(b -> new BookListItemDTO(b.getId(), b.getTitle(), b.getPublisher(), b.getISBN(),
                            b.getPublicationYear())).collect(Collectors.toList());
        } else {
            goveed20.LiteraryAssociationApplication.model.Reader reader = (goveed20.LiteraryAssociationApplication.model.Reader)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Set<String> bookTitles = getBookTitles(reader);

            return bookRepository.findByTitleIn(bookTitles).stream()
                    .map(b -> new BookListItemDTO(b.getId(), b.getTitle(), b.getPublisher(), b.getISBN(),
                            b.getPublicationYear())).collect(Collectors.toList());
        }
    }

    public List<String> getRetailersForBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Book with id '%d' not found", id)));

        return retailerRepository.findAllByBooksContaining(book)
                .stream()
                .map(Retailer::getName)
                .collect(Collectors.toList());
    }

    private Set<String> getBookTitles(BaseUser reader) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(UserRole.READER)) {
            return reader.getTransactions().stream().filter(t -> t.getStatus().equals(TransactionStatus.COMPLETED)
                    && !(t instanceof MembershipTransaction)).map(t -> t.getInvoice().getInvoiceItems().stream()
                    .map(InvoiceItem::getName)).flatMap(Function.identity()).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    public List<String> getGenres() {
        return genreRepository.findAll().stream().map(g -> g.getGenre().getSerbianName()).sorted().collect(Collectors.toList());
    }
}
