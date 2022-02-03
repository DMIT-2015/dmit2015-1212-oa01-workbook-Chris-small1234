package dmit2015.chrissmall.assignment02.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "oscarReviews")
public class OscarReview implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "The Category field is required.")
    @Pattern(regexp = "^film$|^actor$|^actress$|^editing$|^effects$",
            message = "The category must match the regular expression '^film$|^actor$|^actress$|^editing$|^effects$'.")
    private String category;

    @Column(nullable = false)
    @NotBlank(message = "The Nominee field is required")
    private String nominee;

    @Column(nullable = false)
    @NotNull(message = "The Review field is required")
    private String review;

    @Column(nullable = false)
    @NotNull(message = "The Username field is required")
    private String username;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDateTime;

    @PrePersist
    private void beforePersist() {
        createdDateTime = LocalDateTime.now();
        lastModifiedDateTime = createdDateTime;
    }

    @PreUpdate
    private void beforeUpdate() {
        lastModifiedDateTime = LocalDateTime.now();
    }
}