import org.junit.jupiter.api.Test;

class X {
    @Test
    void funcBefore() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void func() {
        String value = "Hello";
        <caret>assertEquals(5, value.length);
    }

    @Test
    void funcAfter() {
        assertEquals(2, 1 + 1);
    }
}