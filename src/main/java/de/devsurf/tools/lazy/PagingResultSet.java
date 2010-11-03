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
package de.devsurf.tools.lazy;

import java.util.List;

public interface PagingResultSet<T> {
	/**
	 * This method should return a new list with the next entries, but it
	 * doesn't guarantee that the amount of returns object is exactly like the
	 * count settings for the ResultSet.
	 * 
	 * @return A List of new Entries. The size can differ from the count
	 *         settings of the ResultSet.
	 */
	List<T> get() throws PagingException;

	/**
	 * This method should return a new list with the next entries, but it
	 * doesn't guarantee that the amount of returns object is exactly like the
	 * count param.
	 * 
	 * @param count
	 *            Parameter which should indicate how much elements should be
	 *            returned.
	 * @return A List of new Entries. The size can differ from the count
	 *         parameter.
	 */
	List<T> get(int count) throws PagingException;

	public class DefaultPagingResultSet<T> implements PagingResultSet<T> {
		private List<T> objects;

		public DefaultPagingResultSet(List<T> objects) {
			super();
			this.objects = objects;
		}

		@Override
		public List<T> get() throws PagingException {
			return objects;
		}

		@Override
		public List<T> get(int count) throws PagingException {
			return objects;
		}
	}

	public class PagingException extends Exception {
		private static final long serialVersionUID = -5845796716253301526L;

		public PagingException() {
			super();
		}

		public PagingException(String message, Throwable cause) {
			super(message, cause);
		}

		public PagingException(String message) {
			super(message);
		}

		public PagingException(Throwable cause) {
			super(cause);
		}
	}
}
