/**
 * Copyright (C) 2010 Daniel Manzke <daniel.manzke@saperion.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devsurf.tools.io;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyInputStream extends FilterInputStream {
	private final ByteArrayOutputStream _copyTo = new ByteArrayOutputStream();

	public CopyInputStream(InputStream in) {
		super(in);
	}

	@Override
	public int read() throws IOException {
		int ch = in.read();
		if (ch != -1) {
			_copyTo.write(ch);
		}
		return ch;
	}

	@Override
	public synchronized int read(byte[] b, int off, int len) throws IOException {
		int size = super.read(b, off, len);
		if (size != -1) {
			_copyTo.write(b, off, size);
		}

		return size;
	}

	public byte[] toByteArray() {
		return _copyTo.toByteArray();
	}
}
