package com.practicaldime.jetty.gql.websock;

public class LinkCreated {

	private final String id;
    private final String url;
    private final String postedBy;
    
	public LinkCreated(String id, String url, String postedBy) {
		super();
		this.id = id;
		this.url = url;
		this.postedBy = postedBy;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getPostedBy() {
		return postedBy;
	}    
}
