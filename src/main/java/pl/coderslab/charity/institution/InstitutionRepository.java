package pl.coderslab.charity.institution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.model.Institution;

import java.util.List;


public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    //@Query("SELECT i FROM Institution i WHERE i.archive=false")
    List<Institution> findAllByArchiveIsFalse();


}
