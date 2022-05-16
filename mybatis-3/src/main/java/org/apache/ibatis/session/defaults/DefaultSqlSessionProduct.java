package org.apache.ibatis.session.defaults;


import java.util.List;
import org.apache.ibatis.cursor.Cursor;
import java.io.IOException;
import org.apache.ibatis.exceptions.ExceptionFactory;
import java.util.ArrayList;

public class DefaultSqlSessionProduct {
	private List<Cursor<?>> cursorList;

	public void closeCursors() {
		if (cursorList != null && !cursorList.isEmpty()) {
			for (Cursor<?> cursor : cursorList) {
				try {
					cursor.close();
				} catch (IOException e) {
					throw ExceptionFactory.wrapException("Error closing cursor.  Cause: " + e, e);
				}
			}
			cursorList.clear();
		}
	}

	public <T> void registerCursor(Cursor<T> cursor) {
		if (cursorList == null) {
			cursorList = new ArrayList<>();
		}
		cursorList.add(cursor);
	}
}