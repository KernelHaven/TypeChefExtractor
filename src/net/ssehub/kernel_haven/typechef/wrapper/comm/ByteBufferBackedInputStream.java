/*
 * Copyright 2017-2018 University of Hildesheim, Software Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ssehub.kernel_haven.typechef.wrapper.comm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * An Adapter to wrap an {@link ByteBuffer} into an {@link InputStream}.
 * <p>
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
