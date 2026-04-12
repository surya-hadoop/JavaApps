import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ContactMigratorTest {

    @Test
    public void testParseLine_ValidData() {
        String line = "Ava Johnson,ava.johnson@example.com,555-0101";
        // Logic to test (we'll move parsing to a helper class next)
        String[] parts = line.split(",", -1);

        assertEquals("Ava Johnson", parts[0].trim());
        assertEquals("ava.johnson@example.com", parts[1].trim());
    }
}