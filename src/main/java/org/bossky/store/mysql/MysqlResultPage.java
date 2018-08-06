package org.bossky.store.mysql;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bossky.common.support.AbstractResultPage;
import org.bossky.store.Storeble;

/**
 * 基于mysql的结果集
 * 
 * @author bo
 *
 * @param <T>
 */
public class MysqlResultPage<T extends Storeble> extends AbstractResultPage<T> {
	MysqlStore<T> store;
	String where;
	int count = -1;
	/** 当前结果集 */
	protected List<T> currentResult = Collections.emptyList();
	/** 当前结果位置 */
	protected int index = 0;

	protected MysqlResultPage(MysqlStore<T> store, String where) {
		this.store = store;
		this.where = where;
	}

	@Override
	public void setPageSize(int size) {
		super.setPageSize(size);
		gotoPage(getPage());
	}

	@Override
	public boolean gotoPage(int page) {
		this.page = page;
		int startIndex = (page - 1) * getPageSize();
		int endIndex = startIndex + getPageSize();
		currentResult = store.search(where, startIndex, endIndex);
		return !currentResult.isEmpty();
	}

	@Override
	public int getCount() {
		if (count == -1) {
			this.count = doGetCount();
		}
		return count;
	}

	/**
	 * 获取结果数
	 * 
	 * @return
	 */
	protected int doGetCount() {
		return store.count(where);
	}

	@Override
	public T next() {
		return currentResult.get(index++);
	}

	@Override
	public boolean hasNext() {
		return index < currentResult.size();
	}

	@Override
	public Iterator<T> iterator() {
		return currentResult.iterator();
	}

}
