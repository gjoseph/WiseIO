/*
 * Some License
 * 2009
 */
package net.incongru.wiseio;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A few simple utility methods using this package's API, thus also serving as examples. 
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class IOUtil {
    /**
     * Loads properties from an InputStream, closes it and returns.
     */
    public static Properties loadProperties(final InputStream in) throws IOException {
        final Properties props = new Properties();

        new ClosingIO<InputStream>(in, new IOOperation() {
            public void op() throws IOException {
                props.load(in);
            }
        }).withClose().exec();

        return props;
    }

}
