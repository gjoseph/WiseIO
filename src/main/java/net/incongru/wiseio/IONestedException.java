/*
 * Some License
 * 2009
 */
package net.incongru.wiseio;

import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class IONestedException extends IOException {
    private final IOException cause;

    IONestedException(String message, IOException cause) {
        super(message);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
