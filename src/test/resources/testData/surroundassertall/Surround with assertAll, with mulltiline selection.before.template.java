import static org.junit.jupiter.api.Assertions.assertEquals;

class X {
    @Test
    void f() {
        String value = "Hello";
<selection><caret>        assertEquals(5, value.length);
        assertEquals("H", value.charAt(0));</selection>
    }
}