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
import java.io.IOException;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ClosingIOTest {

    @Test
    public void emptyIOJustExecutesTheOperation() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final Closeable flow = createStrictMock(Closeable.class);
        op.op();

        replay(op, flow);
        new ClosingIO<Closeable>(flow, op).exec();
        verify(op, flow);
    }

    @Test
    public void noExceptionThenClose() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final Closeable flow = createStrictMock(Closeable.class);
        op.op();
        flow.close();

        replay(op, flow);
        new ClosingIO<Closeable>(flow, op).withClose().exec();
        verify(op, flow);
    }

    @Test
    public void operationCausesIOExceptionAndWeGetIt() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final Closeable flow = createStrictMock(Closeable.class);
        op.op();
        expectLastCall().andThrow(new IOException("Operation failed"));
        flow.close();

        replay(op, flow);
        try {
            new ClosingIO<Closeable>(flow, op).withClose().exec();
            fail("Test should have failed");
        } catch (IOException e) {
            assertEquals("Operation failed", e.getMessage());
        }
        verify(op, flow);
    }

    @Test
    public void closeCausesIOExceptionAndWeGetIt() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final Closeable flow = createStrictMock(Closeable.class);
        op.op();
        flow.close();
        expectLastCall().andThrow(new IOException("Closing failed"));

        replay(op, flow);
        try {
            new ClosingIO<Closeable>(flow, op).withClose().exec();
            fail("Test should have failed");
        } catch (IOException e) {
            assertEquals("Could not close: Closing failed", e.getMessage());
        }
        verify(op, flow);
    }

    @Test
    public void operationAndCloseBothCauseIOExceptionAndWeGetBothExceptions() throws IOException {
        final IOOperation op = createStrictMock(IOOperation.class);
        final Closeable flow = createStrictMock(Closeable.class);
        op.op();
        expectLastCall().andThrow(new IOException("Operation failed"));
        flow.close();
        expectLastCall().andThrow(new IOException("Closing failed"));

        replay(op, flow);
        try {
            new ClosingIO<Closeable>(flow, op).withClose().exec();
            fail("Test should have failed");
        } catch (IOException e) {
            assertEquals("Operation failed; an IOException was also thrown when closing: Closing failed", e.getMessage());
        }
        verify(op, flow);
    }
}
