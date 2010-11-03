/**
 * Copyright (C) 2010 Daniel Manzke <daniel.manzke@googlemail.com>
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
package de.devsurf.tools.lazy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LazyByteArrayInputStream extends InputStream {
	private boolean started;
	private InputStream _delegate;
	private LazyStrategy<byte[]> _strategy;

	public LazyByteArrayInputStream(LazyStrategy<byte[]> strategy) {
		_strategy = strategy;
	}

	@Override
	public void close() throws IOException {
		_delegate.close();
		super.close();
	}

	@Override
	public int read() throws IOException {
		if (!started) {
			try {
				_delegate = new ByteArrayInputStream(_strategy.get());
			} catch (Exception e) {
				throw new IOException(e);
			}
			started = true;
		}
		return _delegate.read();
	}

}
