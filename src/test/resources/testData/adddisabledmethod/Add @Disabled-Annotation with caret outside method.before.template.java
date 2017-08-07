class X {
    @Test
    void<caret> f() {
        String value = "Hello";
        assertEquals(5, value.length);
    }
}