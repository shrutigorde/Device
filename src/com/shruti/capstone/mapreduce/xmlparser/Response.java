package com.shruti.capstone.mapreduce.xmlparser;
import java.util.ArrayList;
import java.util.List;

public class Response implements Comparable<Response> {
	private List<String> parameters = new ArrayList<String>();
	private String title;
	private String activity;
	private String kind;
	private String explain;

	public String getActivity() {
		return kind;
	}

	public void setActivity(String actionType) {
		this.kind = actionType;
	}

	public String getAct() {
		return activity;
	}

	public void setAct(String actionClass) {
		this.activity = actionClass;
	}

	public String getTilte() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public List<String> getValues() {
		return parameters;
	}

	public void setValues(List<String> params) {
		this.parameters = params;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String description) {
		this.explain = description.trim();
	}

	public Response duplicate() {
		Response dup = new Response();
		dup.title = title;
		dup.parameters = parameters;
		dup.explain = explain;
		return dup;
	}

	@Override
	public String toString() {
		StringBuilder strg = new StringBuilder();
		strg.append("Name: ");
		strg.append(title);
		strg.append('\n');
		strg.append("Parameters: ");
		strg.append(this.getValues());
		strg.append('\n');
		strg.append("Description: ");
		strg.append(explain);
		return strg.toString();
	}

	@Override
	public int hashCode() {
		final int digit = 31;
		int r = 1;
		r = digit * r + ((title == null) ? 0 : title.hashCode());
		r = digit * r
				+ ((explain == null) ? 0 : explain.hashCode());
		r = digit * r + ((parameters == null) ? 0 : parameters.hashCode());
		return r;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Response response = (Response) object;
		if (title == null) {
			if (response.title != null)
				return false;
		} else if (!title.equals(response.title))
			return false;
		if (explain == null) {
			if (response.explain != null)
				return false;
		} else if (!explain.equals(response.explain))
			return false;
		if (parameters == null) {
			if (response.parameters != null)
				return false;
		} else if (!parameters.equals(response.parameters))
			return false;
		return true;
	}

	public int compareTo(Response another) {
		if (another == null)
			return 1;
		// implement sorting logic here
		return 1;
	}
}
