import static org.junit.jupiter.api.Assertions.assertEquals;

class X {
    @Test
    void f() {
        String value = "Hello";
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertEquals(5, value.length)
        );
    }
}