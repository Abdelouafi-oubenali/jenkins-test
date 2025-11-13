package demo;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testGreet() {
        App app = new App();
        assertEquals("Hello, Jenkins!", app.greet("Jenkins")); //commit pour test
    }

    @Test
    public void testGreetEmpty() {
        App app = new App();
        assertEquals("Hello, World!", app.greet(""));
        System.out.println("hello test jenkins ");
    }

    @Test
    public void testAdd() {
        App app = new App();
        assertEquals(5, app.add(2, 3));
    }
}