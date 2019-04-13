package com.practicaldime.jetty.gql.websock;

public class LinkCreated {

	private final String id;
    private final String url;
    private final String description;
    
	public LinkCreated(String id, String url, String description) {
		super();
		this.id = id;
		this.url = url;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getDescription() {
		return description;
	}    
}
