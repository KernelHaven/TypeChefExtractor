package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * An Adapter to wrap an {@link ByteBuffer} into an {@link OutputStream}.<br/>
 * Code comes from <a href="https://stackoverflow.com/a/6603018">https://stackoverflow.com/a/6603018</a>.
 * @author El-Sharkawy
 *
 */
public class ByteBufferBackedOutputStream extends OutputStream {
    private ByteBuffer buf;

    /**
     * Sole constructor to wrap an {@link ByteBuffer} into an {@link OutputStream}.
     * @param buf The buffer to wrap
     */
    public ByteBufferBackedOutputStream(ByteBuffer buf) {
        this.buf = buf;
    }

    @Override
    public void write(int aByte) throws IOException {
        buf.put((byte) aByte);
    }

    @Override
    public void write(byte[] bytes, int off, int len) throws IOException {
        buf.put(bytes, off, len);
    }

}
