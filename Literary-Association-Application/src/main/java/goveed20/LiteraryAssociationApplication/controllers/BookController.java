package goveed20.LiteraryAssociationApplication.controllers;

import goveed20.LiteraryAssociationApplication.dtos.BookListItemDTO;
import goveed20.LiteraryAssociationApplication.dtos.SearchQueryDTO;
import goveed20.LiteraryAssociationApplication.elasticsearch.units.BookIndexingUnit;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasAuthority('READER')")
    @PostMapping("/search")
    public ResponseEntity<Page<BookIndexingUnit>> searchBooks(@RequestBody SearchQueryDTO searchQuery) {
        return new ResponseEntity<>(bookService.searchBooks(searchQuery), HttpStatus.OK);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<String>> getGenres() {
        return new ResponseEntity<>(bookService.getGenres(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READER') or hasAuthority('WRITER')")
    @GetMapping
    public ResponseEntity<List<BookListItemDTO>> getBooks() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READER') or hasAuthority('WRITER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{bookTitle}/download")
    @PreAuthorize("hasAuthority('EDITOR') or hasAuthority('READER') or hasAuthority('LECTOR') or hasAuthority('WRITER')")
    public ResponseEntity<?> downloadBook(@PathVariable String bookTitle) {
        try {
            return bookService.downloadBook(bookTitle);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/my-books")
    @PreAuthorize("hasAuthority('WRITER') or hasAuthority('READER')")
    public ResponseEntity<Object> getMyBooks() {
        try {
            return new ResponseEntity<>(bookService.getMyBooks(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/retailer")
    public ResponseEntity<?> getRetailersForBook(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(bookService.getRetailersForBook(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
