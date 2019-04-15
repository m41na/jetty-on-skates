package com.practicaldime.jetty.gql.dao;

import java.util.ArrayList;
import java.util.List;

import com.practicaldime.jetty.gql.model.Link;
import com.practicaldime.jetty.gql.model.LinkFilter;

public class LinkRepositoryMem implements LinkRepository{

	private final List<Link> links;

    public LinkRepositoryMem() {
        links = new ArrayList<>();
        //add some links to start off with
        links.add(new Link("http://howtographql.com", "Your favorite GraphQL page", "test"));
        links.add(new Link("http://graphql.org/learn/", "The official docks", "test"));
    }

    @Override
    public List<Link> getAllLinks() {
        return links;
    }
    
    @Override
    public void saveLink(Link link) {
        links.add(link);
    }

	@Override
	public Link findById(String id) {
		return links.stream().filter(link -> link.getId().equals(id)).findFirst().get();
	}

	@Override
	public List<Link> getAllLinks(LinkFilter filter, int skip, int limit) {
		return links.subList(skip, limit + skip);
	}
}