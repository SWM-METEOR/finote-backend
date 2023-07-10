package kr.co.finote.backend.global.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import kr.co.finote.backend.global.entity.State;

// TODO : State가 @Enumerate(EnumType.STRING)이 됨에 따라 필요하지 않을 때 삭제
@Converter
public class StateAttributeConverter implements AttributeConverter<State, Integer> {

    @Override
    public Integer convertToDatabaseColumn(State state) {
        Integer databaseData = null;
        if (State.TRUE.equals(state)) {
            databaseData = 1;
        } else if (State.FALSE.equals(state)) {
            databaseData = 0;
        }
        return databaseData;
    }

    @Override
    public State convertToEntityAttribute(Integer code) {
        State entityAttribute = null;
        if (1 == code) {
            entityAttribute = State.TRUE;
        } else if (0 == code) {
            entityAttribute = State.FALSE;
        }
        return entityAttribute;
    }
}
