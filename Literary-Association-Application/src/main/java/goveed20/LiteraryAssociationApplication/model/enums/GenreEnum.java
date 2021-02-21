package goveed20.LiteraryAssociationApplication.model.enums;

import lombok.Getter;

@Getter
public enum GenreEnum {
    ADVENTURE("Avantura"),
    FANTASY("Fantastika"),
    MYSTERY("Misterija"),
    HISTORICAL("Istorijski"),
    HORROR("Horor"),
    ROMANCE("Ljubavni"),
    SCIFI("Naučna fantastika"),
    THRILLER("Triler"),
    COOKBOOKS("Kuvar"),
    CRIME("Krimi"),
    CLASSICS("Klasična književnost"),
    SCIENCE("Nauka"),
    EROTIC("Erotika");

    public final String serbianName;

    GenreEnum(String serbianName) {
        this.serbianName = serbianName;
    }
}
