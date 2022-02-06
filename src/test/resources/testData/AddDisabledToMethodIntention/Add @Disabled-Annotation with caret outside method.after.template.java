import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class X {
    @Test
    @org.junit.jupiter.api.Disabled("")
    void f() {
        String value = "Hello";
        assertEquals(5, value.length);
    }
}