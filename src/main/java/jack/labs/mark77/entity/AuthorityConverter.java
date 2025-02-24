package jack.labs.mark77.entity;

import jack.labs.mark77.dto.Authority;
import jakarta.persistence.AttributeConverter;

public class AuthorityConverter implements AttributeConverter<Authority, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Authority authority) {
        return authority.getGrade();
    }

    @Override
    public Authority convertToEntityAttribute(Integer integer) {
        return Authority.valueOf(integer);
    }
}
