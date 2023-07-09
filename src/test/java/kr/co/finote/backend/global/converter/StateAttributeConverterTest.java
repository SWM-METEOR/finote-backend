package kr.co.finote.backend.global.converter;

import kr.co.finote.backend.global.entity.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateAttributeConverterTest {

    private final StateAttributeConverter underTest = new StateAttributeConverter();

    @Test
    void testConvertToDatabaseColumn_whenStateIsActive() {
        assertEquals(1, underTest.convertToDatabaseColumn(State.ACTIVE));
    }
}
