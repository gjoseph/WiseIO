package net.incongru.wiseio;

import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface IO<T> {
    void exec() throws IOException;
}
