@Disabled("")
class X {
    @Test
    void f() {
        String value = "Hello";
        <caret>assertEquals(5, value.length);
    }
}