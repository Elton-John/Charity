package pl.coderslab.charity.institution;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.model.Institution;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class InstitutionService {
    private InstitutionRepository institutionRepository;


    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAllActive();
    }


    public void create(Institution institution) {
        institution.setArchive(false);
        institutionRepository.save(institution);
    }


    public Institution getOneOrThrow(Long id) {
        return institutionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    public void save(Institution institution) {
        institutionRepository.save(institution);
    }


    public void delete(Long id) {
        Institution institution = institutionRepository.getOne(id);
        institution.setArchive(true);
        institutionRepository.save(institution);
    }
}
