class X {
    @Test
    @Disabled("")
    void f() {
        String value = "Hello";
        <caret>assertEquals(5, value.length);
    }
}