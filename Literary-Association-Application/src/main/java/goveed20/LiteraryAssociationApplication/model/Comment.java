package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.CommentType;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentType type;

    @ManyToOne(optional = false)
    private BaseUser user;

    @ManyToMany
    @Builder.Default
    private Set<ApplicationPaper> applicationPapers = new HashSet<>();
}
