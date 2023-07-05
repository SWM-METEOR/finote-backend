package kr.co.finote.backend.global.converter;

import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.global.entity.State;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StateAttributeConverter implements AttributeConverter<State, Integer> {

    @Override
    public Integer convertToDatabaseColumn(State state) {
        Integer databaseData = null;
        if (State.ACTIVE.equals(state)) {
            databaseData = 1;
        } else if (State.INACTIVE.equals(state)) {
            databaseData = 0;
        }
        return databaseData;

    }

    @Override
    public State convertToEntityAttribute(Integer code) {
        State entityAttribute = null;
        if (1 == code) {
            entityAttribute = State.ACTIVE;
        } else if (0 == code) {
            entityAttribute = State.INACTIVE;
        }
        return entityAttribute;
    }
}
