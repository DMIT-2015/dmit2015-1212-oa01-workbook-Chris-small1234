package dmit2015.chrissmall.assignment02.repository;

import dmit2015.chrissmall.assignment02.config.ApplicationConfig;
import dmit2015.chrissmall.assignment02.entity.OscarReview;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ArquillianExtension.class)
class OscarReviewRepositoryIT {

    @Inject
    private OscarReviewRepository _oscarReviewRepository;

    static OscarReview currentOscarReview; 

    @Deployment
    public static WebArchive createDeployment() {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");

        return ShrinkWrap.create(WebArchive.class,"test.war")
                .addAsLibraries(pomFile.resolve("org.hsqldb:hsqldb:2.6.1").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest:2.2").withTransitivity().asFile())
                .addClass(ApplicationConfig.class)
                .addClasses(OscarReview.class, OscarReviewRepository.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/sql/import-data.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml");
    }

    @Order(6)
    @Test
    void shouldFailToCreate() {
        OscarReview emptyOscarReview = new OscarReview();
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            _oscarReviewRepository.add(emptyOscarReview);
        });
        assertTrue(exception.getMessage().contains("The Nominee field is required"));
    }

    @Order(2)
    @Test
    void shouldCreate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        currentOscarReview = new OscarReview();
        currentOscarReview.setCategory("editing");
        currentOscarReview.setNominee("Max");
        currentOscarReview.setReview("Poor");
        currentOscarReview.setUsername("reviewer@gmail.com");
        currentOscarReview.setCreatedDateTime(LocalDateTime.parse("2021-01-21 09:00:09", formatter));
        currentOscarReview.setLastModifiedDateTime(LocalDateTime.parse("2021-02-02T08:00:00"));
        _oscarReviewRepository.add(currentOscarReview);

        Optional<OscarReview> optionalOscarReview = _oscarReviewRepository.findById(currentOscarReview.getId());
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingOscarReview = optionalOscarReview.get();
        assertNotNull(existingOscarReview);
        assertEquals(currentOscarReview.getNominee(), existingOscarReview.getNominee());
        assertEquals(currentOscarReview.getReview(), existingOscarReview.getReview());
        assertEquals(currentOscarReview.getUsername(), existingOscarReview.getUsername());
        long lastModifedDateTimeDifferce = currentOscarReview.getLastModifiedDateTime().until(existingOscarReview.getLastModifiedDateTime(), ChronoUnit.MINUTES);
        assertEquals(0, lastModifedDateTimeDifferce);
    }

    @Order(3)
    @Test
    void shouldFindOne() {
        final Long OscarReviewId = currentOscarReview.getId();
        Optional<OscarReview> optionalOscarReview = _oscarReviewRepository.findById(OscarReviewId);
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingOscarReview = optionalOscarReview.get();
        assertNotNull(existingOscarReview);
        assertEquals(currentOscarReview.getCategory(), existingOscarReview.getCategory());
        assertEquals(currentOscarReview.getNominee(), existingOscarReview.getNominee());
        assertEquals(currentOscarReview.getReview(), existingOscarReview.getReview());
        assertEquals(currentOscarReview.getUsername(), existingOscarReview.getUsername());
        long lastModifedDateTimeDifferce = currentOscarReview.getLastModifiedDateTime().until(existingOscarReview.getLastModifiedDateTime(), ChronoUnit.MINUTES);
        assertEquals(0, lastModifedDateTimeDifferce);
    }

    @Order(1)
    @Test
    void shouldFindAll() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
        List<OscarReview> queryResultList = _oscarReviewRepository.findAll();
        assertEquals(4, queryResultList.size());

        OscarReview firstOscarReview = queryResultList.get(0);
        assertEquals("editing", firstOscarReview.getCategory());
        assertEquals("Harry", firstOscarReview.getNominee());
        assertEquals("good", firstOscarReview.getReview());
        assertEquals("reviewer@gmail.com", firstOscarReview.getUsername());

        OscarReview lastOscarReview = queryResultList.get(queryResultList.size() - 1);
        assertEquals("film", lastOscarReview.getCategory());
        assertEquals("Harry Potter", lastOscarReview.getNominee());
        assertEquals("good", lastOscarReview.getReview());
        assertEquals("reviewer@gmail.com", lastOscarReview.getUsername());

        queryResultList.forEach(System.out::println);
    }

    @Order(4)
    @Test
    void shouldUpdate() {
        currentOscarReview.setNominee("William Dafoe");
        currentOscarReview.setReview("Outstanding");
        currentOscarReview.setUsername("Steven");
        currentOscarReview.setLastModifiedDateTime(LocalDateTime.parse("2021-03-16T08:00:05"));
        _oscarReviewRepository.update(currentOscarReview);

        Optional<OscarReview> optionalUpdatedOscarReview = _oscarReviewRepository.findById(currentOscarReview.getId());
        assertTrue(optionalUpdatedOscarReview.isPresent());
        OscarReview updatedOscarReview = optionalUpdatedOscarReview.get();
        assertNotNull(updatedOscarReview);
        assertEquals(currentOscarReview.getNominee(), updatedOscarReview.getNominee());
        assertEquals(currentOscarReview.getReview(), updatedOscarReview.getReview());
        assertEquals(currentOscarReview.getUsername(), updatedOscarReview.getUsername());
    }

    @Order(5)
    @Test
    void shouldDelete() {
        final Long OscarReviewId = currentOscarReview.getId();
        Optional<OscarReview> optionalOscarReview = _oscarReviewRepository.findById(OscarReviewId);
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingOscarReview = optionalOscarReview.get();
        assertNotNull(existingOscarReview);
        _oscarReviewRepository.remove(existingOscarReview.getId());
        optionalOscarReview = _oscarReviewRepository.findById(OscarReviewId);
        assertTrue(optionalOscarReview.isEmpty());
    }
}
