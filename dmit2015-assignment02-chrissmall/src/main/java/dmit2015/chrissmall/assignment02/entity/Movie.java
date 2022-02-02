package dmit2015.chrissmall.assignment02.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "oscarReviews")
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "The Category field is required.")
    @Pattern.List({
            @Pattern(regexp="film", message = "The category can only be one of these five fields: film, actor, actress, editing, or effects"),
            @Pattern(regexp="actor", message = "The category can only be one of these five fields: film, actor, actress, editing, or effects"),
            @Pattern(regexp="actress", message = "The category can only be one of these five fields: film, actor, actress, editing, or effects"),
            @Pattern(regexp="editing", message = "The category can only be one of these five fields: film, actor, actress, editing, or effects"),
            @Pattern(regexp="effects", message = "The category can only be one of these five fields: film, actor, actress, editing, or effects")
    })
    private String category;
    

    @Column(nullable = false)
    @NotNull(message = "The Release Date field is required")
    private LocalDate releaseDate;
    @DecimalMin(value = "1.00", message = "The price must be a number between 0.00 and 100.00.")
    @DecimalMax(value = "100.00", message = "The price must be a number between 0.00 and 100.00.")
    private BigDecimal price;
    @Column(nullable = false, length = 30)
    @NotBlank(message = "The field Genre is required.")
    @Pattern(regexp = "^[A-Z]+[a-zA-Z\\s]*$",  // Must only use letters.
            // The first letter is required to be uppercase. White space, numbers, and special characters are not allowed.
            message = "The field Genre must match the regular expression '^[A-Z]+[a-zA-Z]*$'.")
    private String genre;
    @Column(nullable = false, length = 5)
    @NotBlank(message = "The field Rating is required.")
    @Pattern(regexp = "^[A-Z]+[a-zA-Z0-9\"\"'\\s-]*$", // The first character can be an uppercase letter
            // Allows special characters and numbers in subsequent spaces. PG-13 is valid but fails for a Genre
            message = "The field Rating must match the regular expression '^[A-Z]+[a-zA-Z0-9\"\"'\\s-]*$'.")
    private String rating; // G, PG, PG-13, R, NC-17

    @Version
    private Integer version;

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