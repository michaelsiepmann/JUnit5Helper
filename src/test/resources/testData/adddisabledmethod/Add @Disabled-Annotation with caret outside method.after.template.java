class X {
    @Test
    @Disabled("")
    void f() {
        String value = "Hello";
        assertEquals(5, value.length);
    }
}