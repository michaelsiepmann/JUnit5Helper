import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class X {
    @Test
    public void testCollectForModel_forModelWithOwnGettersExtendingModel_returnsRightGetters() {
        Collection<Getter> getters = subject.collectForModel(new DummyModelWithOwnGettersExtending());
        assertMethodNames(getters, "dummyString", "dummyBoolean", "dummyDouble");

        List<Getter> orderedGetters = new ArrayList<>(getters);
        orderedGetters.sort(new GetterComparator());

        assertAll(
                () -> assertTrue((Boolean) orderedGetters.get(0).invoke()),
                () -> assertEquals(EXPECTED_LOWER_VALUE, orderedGetters.get(1).invoke()),
                () -> assertEquals("DummyModel.getDummyString", orderedGetters.get(2).invoke())
        );
    }
}