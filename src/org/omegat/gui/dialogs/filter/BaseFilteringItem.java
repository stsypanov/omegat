package org.omegat.gui.dialogs.filter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by stsypanov on 12.05.2015.
 */
@XmlRootElement(name = "filteringItem")
@XmlType(propOrder = {"apply", "pattern", "example"})
public class BaseFilteringItem {
	private boolean apply;
	private String pattern;
	private String example;

	public BaseFilteringItem() {
	}

	public BaseFilteringItem(boolean apply, String pattern, String example) {
		this.apply = apply;
		this.pattern = pattern;
		this.example = example;
	}

	public boolean isApply() {
		return apply;
	}

	@XmlElement
	public void setApply(boolean apply) {
		this.apply = apply;
	}

	public String getPattern() {
		return pattern;
	}

	@XmlElement
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getExample() {
		return example;
	}

	@XmlElement
	public void setExample(String example) {
		this.example = example;
	}

	@Override
	public String toString() {
		return "BaseFilteringItem{" +
				"apply=" + apply +
				", pattern='" + pattern + '\'' +
				", example='" + example + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BaseFilteringItem that = (BaseFilteringItem) o;

		if (apply != that.apply) return false;
		if (!pattern.equals(that.pattern)) return false;
		return !(example != null ? !example.equals(that.example) : that.example != null);

	}

	@Override
	public int hashCode() {
		int result = (apply ? 1 : 0);
		result = 31 * result + pattern.hashCode();
		result = 31 * result + (example != null ? example.hashCode() : 0);
		return result;
	}
}
