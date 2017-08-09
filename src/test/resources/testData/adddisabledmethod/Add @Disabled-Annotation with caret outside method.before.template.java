import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class X {
    @Test
    void<caret> f() {
        String value = "Hello";
        assertEquals(5, value.length);
    }
}