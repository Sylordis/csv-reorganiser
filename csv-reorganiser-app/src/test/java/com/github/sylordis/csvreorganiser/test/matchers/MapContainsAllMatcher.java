package com.github.sylordis.csvreorganiser.test.matchers;

import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher checking if all entries in one map are contained in the provided map. This Matcher does
 * not match on the order, just on the presence of entries and does not check on the number of
 * entries present in the matched map.
 *
 * @author sylordis
 *
 * @param <K> Type of keys
 * @param <V> Type of values
 */
public class MapContainsAllMatcher<K, V> extends TypeSafeMatcher<Map<K, V>> {

	private Map<K, V> expected;

	public MapContainsAllMatcher(Map<K, V> expected) {
		this.expected = expected;
	}

	@Override
	public void describeTo(Description description) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean matchesSafely(Map<K, V> item) {
		boolean contains = true;
		for (Map.Entry<K, V> entry : expected.entrySet()) {
			if (!item.containsKey(entry.getKey()) || !item.get(entry.getKey()).equals(entry.getValue())) {
				contains = false;
				break;
			}
		}
		return contains;
	}

}