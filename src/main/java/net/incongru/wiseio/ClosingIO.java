package net.incongru.wiseio;

import java.io.Closeable;
import java.io.IOException;

/**
 * Executes an operation, closes the stream, wraps exceptions as needed.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ClosingIO<T extends Closeable> implements IO<T> {
    private final T flow;
    private final IOOperation op;
    private boolean shouldClose = true;

    public ClosingIO(T flow, final IOOperation op) {
        if (flow == null) {
            throw new IllegalArgumentException("Can't process a null flow.");
        }
        if (op == null) {
            throw new IllegalArgumentException("IOOperation can't process be null.");
        }
        this.flow = flow;
        this.op = op;
    }

    public IO<T> withoutClose() {
        this.shouldClose = false;
        return this;
    }

    public void exec() throws IOException {
        // this looks a little contrived - the only reason we're doing is so subclasses can wrap the operation if needed.
        exec(this.flow, this.op);
    }

    protected void exec(final T flow, final IOOperation op) throws IOException {
        IOException processEx = null;
        try {
            op.op();
        } catch (IOException e) {
            processEx = e;
        } finally {
            IOException e = cleanupAndProcessException(processEx, flow);
            if (e != null) {
                throw e;
            }
        }
    }

    /**
     * Do your business here. Since the flow is created outside the scope of the ClosingIO instance,
     * you have access to it, we don't need to pass it around.
     */
//    abstract void operation() throws IOException;
    protected IOException cleanupAndProcessException(IOException e, final T flow) {
        if (shouldClose) {
            try {
                flow.close();
            } catch (IOException closingEx) {
                if (e != null) {
                    // throwing a specific exception with the process-exception nested
                    e = new IONestedException(e.getMessage() + "; an IOException was also thrown when closing: " + closingEx.getMessage(), e);
                } else {
                    // here we could maybe just throw finallyEx ?
                    e = new IONestedException("Could not close: " + closingEx.getMessage(), closingEx);
                }
            }
        }
        return e;
    }
}
