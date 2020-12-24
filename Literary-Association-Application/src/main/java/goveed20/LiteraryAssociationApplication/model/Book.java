package goveed20.LiteraryAssociationApplication.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book extends WorkingPaper implements Purchasable {
    @Column(nullable = false, unique = true)
    private String ISBN;

    @Column(nullable = false)
    private String keywords;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private Integer pages;

    @Column(nullable = false)
    private String publicationPlace;

    @Column(nullable = false)
    private Double price;

    @Builder(builderMethodName = "bookBuilder", toBuilder = true)
    public Book(Long id, String file, String title, Genre genre, String synopsis, WorkingPaperStatus status, String ISBN, String keywords, String publisher, Integer publicationYear, Integer pages, String publicationPlace, Double price) {
        super(id, file, title, genre, synopsis, status);
        this.ISBN = ISBN;
        this.keywords = keywords;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.pages = pages;
        this.publicationPlace = publicationPlace;
        this.price = price;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void setName(String name) {
        this.title = name;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
    }
}