package pl.coderslab.charity.institution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.charity.model.Institution;

public class InstitutionConverter implements Converter<String, Institution> {
    @Autowired
    private InstitutionRepository institutionRepository;

    @Override
    public Institution convert(String source) {
        return institutionRepository.getOne(Long.parseLong(source));
    }
}


