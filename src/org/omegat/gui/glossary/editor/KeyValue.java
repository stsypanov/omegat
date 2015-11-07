package org.omegat.gui.glossary.editor;

/**
 * Created by stsypanov on 28.10.2015.
 */
public class KeyValue<K, V> {

	protected K key;
	protected V value;

	public KeyValue() {
	}

	public KeyValue(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		KeyValue<?, ?> keyValue = (KeyValue<?, ?>) o;

		return !(key != null ? !key.equals(keyValue.key) : keyValue.key != null) &&
				!(value != null ? !value.equals(keyValue.value) : keyValue.value != null);

	}

	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}
}
