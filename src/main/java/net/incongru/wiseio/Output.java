/*
 * Some License
 * 2009
 */
package net.incongru.wiseio;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * Executes an operation, flushes the stream as required, closes the stream, wraps exceptions as needed.
 * 
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class Output<T extends Closeable & Flushable> extends ClosingIO<T> {
    private boolean shouldFlushWhenSuccessful;
    private boolean shouldFlushInFinallyBlock;

    public Output(T flow, IOOperation op) {
        super(flow, op);
    }

    // ugly - if we don't do this, then withClose() can only be called last when building an Output
//    public Output<T> withClose() {
//        return (Output<T>) super.withClose();
//    }

    public Output<T> withFlush() {
        this.shouldFlushInFinallyBlock = true;
        return this;
    }

    public Output<T> withFlushWhenSuccessful() {
        this.shouldFlushWhenSuccessful = true;
        return this;
    }

    @Override
    protected void exec(final T flow, final IOOperation op) throws IOException {
        super.exec(flow, shouldFlushWhenSuccessful ? new Flusher<T>(flow, op) : op);
    }

    @Override
    protected IOException cleanupAndProcessException(IOException e, final T flow) {
        if (shouldFlushInFinallyBlock) {
            try {
                flow.flush();
            } catch (IOException flushingEx) {
                if (e != null) {
                    // throwing a specific exception with the process-exception nested
                    e = new IONestedException(e.getMessage() + "; an IOException was also thrown when flushing: " + flushingEx.getMessage(), e);
                } else {
                    // here we could maybe just throw finallyEx ?
                    e = new IONestedException("Could not flush: " + flushingEx.getMessage(), flushingEx);
                }
            }
        }
        return super.cleanupAndProcessException(e, flow);
    }

    /**
     * A simple IOOperation which delegates to another IOOperation then flushes the stream.
     * @param <T> a Closeable and Flushable
     */
    private class Flusher<T extends Flushable> implements IOOperation {
        private final T flow;
        private final IOOperation op;

        public Flusher(final T flow, final IOOperation op) {
            this.flow = flow;
            this.op = op;
        }

        public void op() throws IOException {
            op.op();
            flow.flush();
        }
    }

//    private final class DelegateToAbstractOpMethodOperation implements IOOperation<T> {
//        public void op(T flow) throws IOException {
//            operation();
//        }
//    }

}
