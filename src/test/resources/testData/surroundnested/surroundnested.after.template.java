import org.junit.jupiter.api.Test;

class X {
    @Test
    void funcBefore() {
        assertEquals(2, 1 + 1);
    }

    @org.junit.jupiter.api.Nested
    @org.junit.jupiter.api.DisplayName("")
    class FuncTest {
        @Test
        void func() {
            String value = "Hello";
            assertEquals(5, value.length);
        }
    }

    @Test
    void funcAfter() {
        assertEquals(2, 1 + 1);
    }
}