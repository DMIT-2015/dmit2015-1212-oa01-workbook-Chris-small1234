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
//                .addAsLibraries(pomFile.resolve("groupId:artifactId:version").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.h2database:h2:2.1.210").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hsqldb:hsqldb:2.6.1").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc:8.4.1.jre11").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc11:21.1.0.0").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest:2.2").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.hibernate:hibernate-core-jakarta:5.6.5.Final").withTransitivity().asFile())
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
        assertTrue(exception.getMessage().contains("The Release Date field is required"));
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
        //currentOscarReview.setLastModifiedDateTime(LocalDateTime.parse("2021-02-02T08:00:00"));
        _oscarReviewRepository.add(currentOscarReview);

        Optional<OscarReview> optionalOscarReview = _oscarReviewRepository.findById(currentOscarReview.getId());
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingOscarReview = optionalOscarReview.get();
        assertNotNull(existingOscarReview);
        assertEquals(currentOscarReview.getNominee(), existingOscarReview.getNominee());
        assertEquals(currentOscarReview.getReview(), existingOscarReview.getReview());
        assertEquals(currentOscarReview.getUsername(), existingOscarReview.getUsername());
        assertEquals(currentOscarReview.getCreatedDateTime(), existingOscarReview.getCreatedDateTime());
        //assertEquals(currentOscarReview.getLastModifiedDateTime(), existingOscarReview.getLastModifiedDateTime());
    }

    @Order(3)
    @Test
    void shouldFindOne() {
        final Long OscarReviewId = currentOscarReview.getId();
        Optional<OscarReview> optionalOscarReview = _oscarReviewRepository.findById(OscarReviewId);
        assertTrue(optionalOscarReview.isPresent());
        OscarReview existingOscarReview = optionalOscarReview.get();
        assertNotNull(existingOscarReview);
        assertEquals(currentOscarReview.getNominee(), existingOscarReview.getNominee());
        assertEquals(currentOscarReview.getReview(), existingOscarReview.getReview());
        assertEquals(currentOscarReview.getUsername(), existingOscarReview.getUsername());
        assertEquals(currentOscarReview.getLastModifiedDateTime(), existingOscarReview.getLastModifiedDateTime());
    }

    @Order(1)
    @Test
    void shouldFindAll() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
        List<OscarReview> queryResultList = _oscarReviewRepository.findAll();
        assertEquals(4, queryResultList.size());

        OscarReview firstOscarReview = queryResultList.get(0);
        assertEquals("When Harry Met Sally", firstOscarReview.getNominee());
        assertEquals("Romantic Comedy", firstOscarReview.getReview());
        assertEquals(7.99, firstOscarReview.getUsername());

        OscarReview lastOscarReview = queryResultList.get(queryResultList.size() - 1);
        assertEquals("Rio Bravo", lastOscarReview.getNominee());
        assertEquals("Western", lastOscarReview.getReview());
        assertEquals("blah", lastOscarReview.getUsername());
        assertEquals(LocalDate.parse("1959-04-15", formatter).toString(), lastOscarReview.getLastModifiedDateTime().toString());

        queryResultList.forEach(System.out::println);
    }

    @Order(4)
    @Test
    void shouldUpdate() {
        currentOscarReview.setNominee("Comedy");
        currentOscarReview.setReview("JDK 16 Release ");
        currentOscarReview.setUsername("PG-13");
        currentOscarReview.setLastModifiedDateTime(LocalDateTime.parse("2021-03-16"));
        _oscarReviewRepository.update(currentOscarReview);

        Optional<OscarReview> optionalUpdatedOscarReview = _oscarReviewRepository.findById(currentOscarReview.getId());
        assertTrue(optionalUpdatedOscarReview.isPresent());
        OscarReview updatedOscarReview = optionalUpdatedOscarReview.get();
        assertNotNull(updatedOscarReview);
        assertEquals(currentOscarReview.getNominee(), updatedOscarReview.getNominee());
        assertEquals(currentOscarReview.getReview(), updatedOscarReview.getReview());
        assertEquals(currentOscarReview.getUsername(), updatedOscarReview.getUsername());

        //assertEquals(currentOscarReview.getReleaseDate(), updatedOscarReview.getReleaseDate());
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
