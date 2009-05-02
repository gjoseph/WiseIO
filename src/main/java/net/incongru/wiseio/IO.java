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
public interface IO<T> {
    void exec() throws IOException;
//    void exec(T flow, IOOperation<T> op) throws IOException;

// TODO : find a good name for this
//    public void execWithRunTimeException(IOOperation op) {
//        op.op();
//    }
}
