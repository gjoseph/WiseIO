/*
 * Some License
 * 2009
 */
package net.incongru.wiseio;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class OutputTest {

    @Test
    public void allGoesWellAndWeFlushTwice() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final FlushableAndCloseable flow = createStrictMock(FlushableAndCloseable.class);
        op.op();
        flow.flush();
        flow.flush();
        flow.close();
        replay(op, flow);
        new Output<FlushableAndCloseable>(flow, op).withFlushWhenSuccessful().withFlush().withClose().exec();
        verify(op, flow);
    }

    @Test
    public void allGoesWellAndWeFlushInFinalBlock() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final FlushableAndCloseable flow = createStrictMock(FlushableAndCloseable.class);
        op.op();
        flow.flush();
        flow.close();
        replay(op, flow);
        new Output<FlushableAndCloseable>(flow, op).withFlush().withClose().exec();
        verify(op, flow);
    }

    @Test
    public void allGoesWellAndWeFlushOnCompletion() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final FlushableAndCloseable flow = createStrictMock(FlushableAndCloseable.class);
        op.op();
        flow.flush();
        flow.close();
        replay(op, flow);
        new Output<FlushableAndCloseable>(flow, op).withFlushWhenSuccessful().withClose().exec();
        verify(op, flow);
    }

    @Test
    public void operationAndFinalFlushAndCloseAllCauseIOExceptionAndWeGetThemAll() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final FlushableAndCloseable flow = createStrictMock(FlushableAndCloseable.class);
        op.op();
        expectLastCall().andThrow(new IOException("Operation failed"));
        flow.flush();
        expectLastCall().andThrow(new IOException("Flushing failed"));
        flow.close();
        expectLastCall().andThrow(new IOException("Closing failed"));

        replay(op, flow);
        try {
            new Output<FlushableAndCloseable>(flow, op).withFlush().withFlushWhenSuccessful().withClose().exec();
            fail("Test should have failed");
        } catch (IOException e) {
            assertEquals("Operation failed; an IOException was also thrown when flushing: Flushing failed; an IOException was also thrown when closing: Closing failed", e.getMessage());
        }
        verify(op, flow);
    }

    @Test
    public void postOperationFlushFailsAndWeStillClose() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final FlushableAndCloseable flow = createStrictMock(FlushableAndCloseable.class);
        op.op();
        flow.flush();
        expectLastCall().andThrow(new IOException("Flushing failed"));
        flow.close();

        replay(op, flow);
        try {
            new Output<FlushableAndCloseable>(flow, op).withFlushWhenSuccessful().withClose().exec();
            fail("Test should have failed");
        } catch (IOException e) {
            assertEquals("Flushing failed", e.getMessage());
        }
        verify(op, flow);
    }

    @Test
    public void bothFlushesWorkBothTheCloseFails() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final FlushableAndCloseable flow = createStrictMock(FlushableAndCloseable.class);
        op.op();
        flow.flush();
        flow.flush();
        flow.close();
        expectLastCall().andThrow(new IOException("Closing failed"));

        replay(op, flow);
        try {
            new Output<FlushableAndCloseable>(flow, op).withFlush().withFlushWhenSuccessful().withClose().exec();
            fail("Test should have failed");
        } catch (IOException e) {
            assertEquals("Could not close: Closing failed", e.getMessage());
        }
        verify(op, flow);
    }

    private static interface FlushableAndCloseable extends Flushable, Closeable {
    }
}
