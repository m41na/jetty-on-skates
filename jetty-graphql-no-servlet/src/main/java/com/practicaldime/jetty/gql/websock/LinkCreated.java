package com.practicaldime.jetty.gql.websock;

public class LinkCreated {

	private final String id;
    private final String url;
    private final String descr;
    
	public LinkCreated(String id, String url, String descr) {
		super();
		this.id = id;
		this.url = url;
		this.descr = descr;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getDescr() {
		return descr;
	}
}
