package net.incongru.wiseio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class IOUtilTest {
    @Test
    public void loadPropertiesFromInputStream() throws IOException {
        final Properties properties = IOUtil.loadProperties(getClass().getResourceAsStream("dummy.properties"));
        assertNotNull(properties);
        assertEquals(2, properties.size());
        assertEquals("bar", properties.getProperty("foo"));
        assertEquals("baz", properties.getProperty("test"));
    }
}
