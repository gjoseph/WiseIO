/*
 * Some License
 * 2009
 */
package net.incongru.wiseio;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public interface IOOperation { // <T extends Closeable>
    void op() throws IOException;
//    void op(T flow) throws IOException;
}
