package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.elasticsearch.services.IndexingService;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import goveed20.LiteraryAssociationApplication.services.CamundaUserService;
import goveed20.LiteraryAssociationApplication.services.LocationService;
import goveed20.LiteraryAssociationApplication.services.PlagiarismService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DummyDataService {

    private static final String booksFolder = "literary-association/Literary-Association-Application/src/main/resources/books/";

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CamundaUserService camundaUserService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private IndexingService indexingService;

    @Autowired
    private PlagiarismService plagiarismService;


    @EventListener(ApplicationReadyEvent.class)
    public void addDummyData() throws IOException {
        indexingService.deleteAllIndexes();

        Location noviSad = locationService.createLocation("Serbia", "Novi Sad");
        Location split = locationService.createLocation("Croatia", "Split");
        Location berlin = locationService.createLocation("Germany", "Berlin");

        if (baseUserRepository.findAllByRole(UserRole.BOARD_MEMBER).isEmpty()) {
            BaseUser boardMember1 = BaseUser.builder()
                    .name("board_member_1_name")
                    .surname("board_member_1_surname")
                    .email("board_member_1@test.com")
                    .username("boardMember1")
                    .password(passwordEncoder.encode("board_member_1"))
                    .verified(true)
                    .role(UserRole.BOARD_MEMBER)
                    .location(noviSad.toBuilder().build())
                    .build();

            BaseUser boardMember2 = BaseUser.builder()
                    .name("board_member_2_name")
                    .surname("board_member_2_surname")
                    .email("board_member_2@test.com")
                    .username("boardMember2")
                    .verified(true)
                    .role(UserRole.BOARD_MEMBER)
                    .password(passwordEncoder.encode("board_member_2"))
                    .location(noviSad.toBuilder().build())
                    .build();

            baseUserRepository.save(boardMember1);
            baseUserRepository.save(boardMember2);

            camundaUserService.createCamundaUser(boardMember1);
            camundaUserService.createCamundaUser(boardMember2);
        }

        if (baseUserRepository.findAllByRole(UserRole.WRITER).isEmpty() && bookRepository.findAll().isEmpty() && retailerRepository.findAll().isEmpty()
                && baseUserRepository.findAllByRole(UserRole.READER).isEmpty()) {

            if (genreRepository.findAll().isEmpty()) {
                Arrays.stream(GenreEnum.values()).forEach(e -> genreRepository.save(new Genre(null, e)));
            }

            Reader reader1 = Reader.readerBuilder()
                    .role(UserRole.READER)
                    .username("reader1")
                    .password(passwordEncoder.encode("password1"))
                    .name("reader1")
                    .surname("reader1")
                    .email("reader1@maildrop.cc")
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .genres(new HashSet<>())
                    .betaReader(false)
                    .location(noviSad.toBuilder().build())
                    .verified(true)
                    .build();

            Reader reader2 = Reader.readerBuilder()
                    .role(UserRole.READER)
                    .username("reader2")
                    .password(passwordEncoder.encode("password2"))
                    .name("reader2")
                    .surname("reader2")
                    .email("reader2@maildrop.cc")
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .genres(new HashSet<>())
                    .betaReader(true)
                    .location(noviSad.toBuilder().build())
                    .verified(true)
                    .build();


            BetaReaderStatus status1 = BetaReaderStatus.builder()
                    .betaGenres(Collections.singleton(genreRepository.findByGenre(GenreEnum.SCIENCE)))
                    .reader(reader2)
                    .build();

            reader2.setBetaReaderStatus(status1);

            Reader reader3 = Reader.readerBuilder()
                    .role(UserRole.READER)
                    .username("reader3")
                    .password(passwordEncoder.encode("password3"))
                    .name("reader3")
                    .surname("reader3")
                    .email("reader3@maildrop.cc")
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .genres(new HashSet<>())
                    .betaReader(true)
                    .location(split.toBuilder().build())
                    .verified(true)
                    .build();

            BetaReaderStatus status2 = BetaReaderStatus.builder()
                    .reader(reader3)
                    .betaGenres(Stream.of(genreRepository.findByGenre(GenreEnum.SCIENCE), genreRepository.findByGenre(GenreEnum.ADVENTURE)).collect(Collectors.toSet()))
                    .build();

            reader3.setBetaReaderStatus(status2);

            Reader reader4 = Reader.readerBuilder()
                    .role(UserRole.READER)
                    .username("reader4")
                    .password(passwordEncoder.encode("password4"))
                    .name("reader4")
                    .surname("reader4")
                    .email("reader4@maildrop.cc")
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .genres(new HashSet<>())
                    .betaReader(true)
                    .location(berlin.toBuilder().build())
                    .verified(true)
                    .build();

            BetaReaderStatus status3 = BetaReaderStatus.builder()
                    .reader(reader4)
                    .betaGenres(new HashSet<>(genreRepository.findAll()))
                    .build();

            reader4.setBetaReaderStatus(status3);

            Writer writer1 = Writer.writerBuilder()
                    .role(UserRole.WRITER)
                    .genres(new HashSet<>())
                    .location(noviSad.toBuilder().build())
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .verified(true)
                    .membershipApproved(true)
                    .workingPapers(new HashSet<>())
                    .books(new HashSet<>())
                    .username("perata")
                    .password(passwordEncoder.encode("Pera1997!"))
                    .name("Pero")
                    .surname("Peric")
                    .email("perata@maildrop.cc")
                    .genres(new HashSet<>())
                    .build();

            Writer writer2 = Writer.writerBuilder()
                    .role(UserRole.WRITER)
                    .genres(new HashSet<>())
                    .location(noviSad.toBuilder().build())
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .verified(true)
                    .membershipApproved(true)
                    .workingPapers(new HashSet<>())
                    .books(new HashSet<>())
                    .username("lazata")
                    .password(passwordEncoder.encode("Laza1997!"))
                    .name("Lazo")
                    .surname("Lazic")
                    .email("lazata@maildrop.cc")
                    .genres(new HashSet<>())
                    .build();

            Writer writer3 = Writer.writerBuilder()
                    .role(UserRole.WRITER)
                    .genres(new HashSet<>())
                    .location(noviSad.toBuilder().build())
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .verified(true)
                    .membershipApproved(true)
                    .workingPapers(new HashSet<>())
                    .books(new HashSet<>())
                    .username("radulata")
                    .password(passwordEncoder.encode("Radule1997!"))
                    .name("Radulo")
                    .surname("Radulic")
                    .email("radulo@maildrop.cc")
                    .genres(new HashSet<>())
                    .build();

            Writer writer4 = Writer.writerBuilder()
                    .role(UserRole.WRITER)
                    .genres(new HashSet<>())
                    .location(noviSad.toBuilder().build())
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .verified(true)
                    .membershipApproved(true)
                    .workingPapers(new HashSet<>())
                    .books(new HashSet<>())
                    .username("gagatata")
                    .password(passwordEncoder.encode("Gago1997!"))
                    .name("Gago")
                    .surname("Gagic")
                    .email("gago@maildrop.cc")
                    .genres(new HashSet<>())
                    .build();

            Writer writer5 = Writer.writerBuilder()
                    .role(UserRole.WRITER)
                    .genres(new HashSet<>())
                    .location(noviSad.toBuilder().build())
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .verified(true)
                    .membershipApproved(true)
                    .workingPapers(new HashSet<>())
                    .books(new HashSet<>())
                    .username("djolatata")
                    .password(passwordEncoder.encode("Djole1997!"))
                    .name("Djole")
                    .surname("Djolic")
                    .email("djole@maildrop.cc")
                    .genres(new HashSet<>())
                    .build();

            Writer writer6 = Writer.writerBuilder()
                    .role(UserRole.WRITER)
                    .genres(new HashSet<>())
                    .location(noviSad.toBuilder().build())
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .verified(true)
                    .membershipApproved(true)
                    .workingPapers(new HashSet<>())
                    .books(new HashSet<>())
                    .username("writer6")
                    .password(passwordEncoder.encode("Writer1997!"))
                    .name("Writer")
                    .surname("Writic")
                    .email("writer6@maildrop.cc")
                    .genres(new HashSet<>())
                    .build();

            camundaUserService.createCamundaUser(writer1);
            camundaUserService.createCamundaUser(writer2);
            camundaUserService.createCamundaUser(writer3);
            camundaUserService.createCamundaUser(writer4);
            camundaUserService.createCamundaUser(writer5);
            camundaUserService.createCamundaUser(writer6);

            Genre fantasy = genreRepository.findByGenre(GenreEnum.FANTASY);
            Genre classic = genreRepository.findByGenre(GenreEnum.CLASSICS);
            Genre science = genreRepository.findByGenre(GenreEnum.SCIENCE);

            Book b1 = Book.bookBuilder()
                    .file(String.format("%sŽivotinjska farma.pdf", booksFolder))
                    .title("Životinjska farma")
                    .synopsis("synopsis1")
                    .genre(classic)
                    .ISBN("0-3818-9816-4")
                    .keywords("dzordz, orvel, klasika")
                    .publisher("Kontrast izdavaštvo")
                    .publicationYear(2014)
                    .pages(108)
                    .publicationPlace("Zemun, Srbija")
                    .price(5.66)
                    .additionalAuthors("Džordž Orvel")
                    .build();
            b1.setWriter(writer1);

            Book b2 = Book.bookBuilder()
                    .file(String.format("%sKrv vilenjaka.pdf", booksFolder))
                    .title("Krv vilenjaka")
                    .synopsis("synopsis1")
                    .genre(fantasy)
                    .ISBN("978-86-7702-225-9")
                    .keywords("veštac, andzej, sapkovski")
                    .publisher("Čarobna knjiga")
                    .publicationYear(2012)
                    .pages(317)
                    .price(9.0)
                    .publicationPlace("Beograd, Srbija")
                    .additionalAuthors("Andžej Sapkovski")
                    .build();
            b2.setWriter(writer2);

            Book b3 = Book.bookBuilder()
                    .file(String.format("%sLiterarno udruženje.pdf", booksFolder))
                    .title("Literarno udruženje")
                    .synopsis("synopsis3")
                    .genre(science)
                    .ISBN("0-6918-9816-4")
                    .keywords("projekat, udd, upp, sep")
                    .publisher("FTN")
                    .publicationYear(2020)
                    .pages(11)
                    .publicationPlace("Novi Sad, Srbija")
                    .price(0.0)
                    .additionalAuthors("Dragan Ivanović, Goran Sladić, Miroslav Zarić")
                    .build();
            b3.setWriter(writer3);

            Book book = Book.bookBuilder()
                    .ISBN("0-8813-8980-3")
                    .publisher("Sezam Book")
                    .title("Prokleta avlija")
                    .publicationYear(2018)
                    .keywords("ivo, andrić, klasika")
                    .pages(117)
                    .publicationPlace("Beograd, Srbija")
                    .genre(classic)
                    .synopsis("synopsis4")
                    .price(11.05)
                    .file(String.format("%sProkleta avlija.pdf", booksFolder))
                    .additionalAuthors("Ivo Andrić")
                    .build();
            book.setWriter(writer4);

            Book book2 = Book.bookBuilder()
                    .ISBN("0-8813-8980-4")
                    .publisher("Kontrast izdavaštvo")
                    .title("Stranac")
                    .publicationYear(2018)
                    .keywords("alber, kami, klasika")
                    .pages(102)
                    .publicationPlace("Zemun, Srbija")
                    .genre(classic)
                    .synopsis("synopsis5")
                    .price(6.29)
                    .file(String.format("%sStranac.pdf", booksFolder))
                    .additionalAuthors("Alber Kami")
                    .build();
            book2.setWriter(writer5);

            Book book3 = Book.bookBuilder()
                    .ISBN("0-8553-8980-4")
                    .publisher("FTN")
                    .title("Upravljanje digitalnim dokumentima")
                    .publicationYear(2014)
                    .keywords("udžbenik, ftn, digitalni dokumenti")
                    .pages(240)
                    .publicationPlace("Novi Sad, Srbija")
                    .genre(science)
                    .synopsis("synopsis6")
                    .price(0.0)
                    .file(String.format("%sUpravljanje digitalnim dokumentima.pdf", booksFolder))
                    .additionalAuthors("Branko Milosavljević, Dragan Ivanović")
                    .build();
            book3.setWriter(writer6);

            bookRepository.save(b1);
            bookRepository.save(b2);
            bookRepository.save(b3);
            bookRepository.save(book);
            bookRepository.save(book2);
            bookRepository.save(book3);

            indexingService.indexBook(b1);
            indexingService.indexBook(b2);
            indexingService.indexBook(b3);
            indexingService.indexBook(book);
            indexingService.indexBook(book2);
            indexingService.indexBook(book3);

            plagiarismService.uploadPaper(b1.getTitle(), b1.getFile(), false);
            plagiarismService.uploadPaper(b2.getTitle(), b2.getFile(), false);
            plagiarismService.uploadPaper(b3.getTitle(), b3.getFile(), false);
            plagiarismService.uploadPaper(book.getTitle(), book.getFile(), false);
            plagiarismService.uploadPaper(book2.getTitle(), book2.getFile(), false);
            plagiarismService.uploadPaper(book3.getTitle(), book3.getFile(), false);

            InvoiceItem item = InvoiceItem.builder().name(b3.getTitle())
                    .price(b3.getPrice()).quantity(1).build();
            Set<InvoiceItem> items = new HashSet<>();
            items.add(item);

            Retailer r = Retailer.builder()
                    .name("Laguna")
                    .email("laguna@maildrop.cc")
                    .books(new HashSet<>(Arrays.asList(b1, b2, b3, book, book2)))
                    .build();

            retailerRepository.save(r);

            Invoice invoice = Invoice.builder().retailer(r).invoiceItems(items).build();

            Transaction transaction = Transaction.builder().completedOn(new Date()).createdOn(new Date())
                    .initializedOn(new Date()).done(true).paidWith("bank-service").total(b3.getPrice()).invoice(invoice)
                    .status(TransactionStatus.COMPLETED).build();
            invoice.setTransaction(transaction);

            reader1.getTransactions().add(transaction);

            baseUserRepository.save(reader1);
            baseUserRepository.save(reader2);
            baseUserRepository.save(reader3);
            baseUserRepository.save(reader4);

            camundaUserService.createCamundaUser(reader1);
            camundaUserService.createCamundaUser(reader2);
            camundaUserService.createCamundaUser(reader3);
            camundaUserService.createCamundaUser(reader4);

            indexingService.indexBetaReader(reader2);
            indexingService.indexBetaReader(reader3);
            indexingService.indexBetaReader(reader4);
        }


        if (baseUserRepository.findAllByRole(UserRole.EDITOR).isEmpty()) {
            BaseUser editor = BaseUser.builder()
                    .role(UserRole.EDITOR)
                    .username("editor")
                    .password(passwordEncoder.encode("Editor1997!"))
                    .email("editor@maildrop.cc")
                    .name("Editor")
                    .surname("Editorovic")
                    .verified(true)
                    .location(noviSad.toBuilder().build())
                    .build();

            BaseUser editor2 = BaseUser.builder()
                    .role(UserRole.EDITOR)
                    .username("mujoalen")
                    .password(passwordEncoder.encode("Editor1997!"))
                    .email("editor2@maildrop.cc")
                    .name("Mujo")
                    .surname("Alen")
                    .verified(true)
                    .location(noviSad.toBuilder().build())
                    .build();

            BaseUser editor3 = BaseUser.builder()
                    .role(UserRole.EDITOR)
                    .username("jurica")
                    .password(passwordEncoder.encode("Editor1997!"))
                    .email("editor3@maildrop.cc")
                    .name("Jurica")
                    .surname("Juric")
                    .verified(true)
                    .location(noviSad.toBuilder().build())
                    .build();

            BaseUser editor4 = BaseUser.builder()
                    .role(UserRole.EDITOR)
                    .username("peronikic")
                    .password(passwordEncoder.encode("Editor1997!"))
                    .email("editor4@maildrop.cc")
                    .name("Pero")
                    .surname("Nikic")
                    .verified(true)
                    .location(noviSad.toBuilder().build())
                    .build();

            baseUserRepository.save(editor);
            baseUserRepository.save(editor2);
            baseUserRepository.save(editor3);
            baseUserRepository.save(editor4);
            camundaUserService.createCamundaUser(editor);
            camundaUserService.createCamundaUser(editor2);
            camundaUserService.createCamundaUser(editor3);
            camundaUserService.createCamundaUser(editor4);
        }

        if (baseUserRepository.findAllByRole(UserRole.LECTOR).isEmpty()) {
            BaseUser lector = BaseUser.builder()
                    .role(UserRole.LECTOR)
                    .username("lector")
                    .password(passwordEncoder.encode("Lector1997!"))
                    .email("lector@maildrop.cc")
                    .name("Lector")
                    .surname("Lectorovic")
                    .verified(true)
                    .location(noviSad.toBuilder().build())
                    .build();

            baseUserRepository.save(lector);
            camundaUserService.createCamundaUser(lector);
        }
        System.out.println("Created dummy data!");
    }
}
