package dmit2015.chrissmall.assignment02.repository;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import dmit2015.chrissmall.assignment02.entity.OscarReview;

@ApplicationScoped
@Transactional
public class OscarReviewRepository {

    @PersistenceContext(unitName = "hsqldatabase-jpa-pu")
    private EntityManager em;

    public void add(OscarReview newOscarReview) {
        em.persist(newOscarReview);
    }

    public void update(OscarReview updatedOscarReview) {
        Optional<OscarReview> optionalMovie = findById(updatedOscarReview.getId());
        if (optionalMovie.isPresent()) {
            OscarReview existingOscarReview = optionalMovie.get();
            existingOscarReview.setCategory(updatedOscarReview.getCategory());
            existingOscarReview.setNominee(updatedOscarReview.getNominee());
            existingOscarReview.setReview(updatedOscarReview.getReview());
            existingOscarReview.setUsername(updatedOscarReview.getUsername());
            em.merge(existingOscarReview);
            em.flush();
        }
    }

    public void remove(Long oscarReviewId) {
        Optional<OscarReview> optionalMovie = findById(oscarReviewId);
        if (optionalMovie.isPresent()) {
            OscarReview existingOscarReview = optionalMovie.get();
            em.remove(existingOscarReview);
            em.flush();
        }
    }

    public Optional<OscarReview> findById(Long oscarReviewId) {
        Optional<OscarReview> optionalOscarReview = Optional.empty();
        try {
            OscarReview querySingleResult = em.find(OscarReview.class, oscarReviewId);
            if (querySingleResult != null) {
                optionalOscarReview = Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return optionalOscarReview;
    }

    public List<OscarReview> findAll() {
        return em.createQuery(
                "SELECT m FROM OscarReview m "
                , OscarReview.class)
                .getResultList();
    }

    public List<OscarReview> findAllOrderByNominee() {
        return em.createQuery(
                "SELECT m FROM OscarReview m ORDER BY m.nominee"
                , OscarReview.class)
                .getResultList();
    }
}