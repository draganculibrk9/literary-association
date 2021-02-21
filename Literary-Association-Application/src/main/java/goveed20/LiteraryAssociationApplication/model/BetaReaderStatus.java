package goveed20.LiteraryAssociationApplication.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class BetaReaderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private Set<Genre> betaGenres;

    @Column(nullable = false)
    @Builder.Default
    private Integer penaltyPoints = 0;

    @OneToOne(optional = false)
    private Reader reader;

    @ManyToMany
    private Set<WorkingPaper> betaReaderPapers;
}
