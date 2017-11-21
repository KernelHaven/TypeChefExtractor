package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * An Adapter to wrap an {@link ByteBuffer} into an {@link InputStream}.<br/>
 * Code comes from <a href="https://stackoverflow.com/a/6603018">https://stackoverflow.com/a/6603018</a>.
 * @author El-Sharkawy
 *
 */
public class ByteBufferBackedInputStream extends InputStream {
    private ByteBuffer buf;

    /**
     * Sole constructor to wrap a {@link ByteBuffer} into an {@link InputStream}.
     * @param buf The buffer to wrap.
     */
    public ByteBufferBackedInputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    @Override
    public int read() throws IOException {
        int result = !buf.hasRemaining() ? -1 : buf.get() & 0xFF;
        return result;
    }

    @Override
    public int read(byte[] bytes, int off, int len) throws IOException {
        int result;
        if (!buf.hasRemaining()) {
            result = -1;
        } else {
            len = Math.min(len, buf.remaining());
            buf.get(bytes, off, len);
            result = len;
        }

        return result;
    }
}
