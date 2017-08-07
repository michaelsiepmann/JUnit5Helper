import static org.junit.jupiter.api.Assertions.assertEquals;

class X {
    @Test
    void f() {
        String value = "Hello";
        <caret>assertEquals(5, value.length);
    }
}