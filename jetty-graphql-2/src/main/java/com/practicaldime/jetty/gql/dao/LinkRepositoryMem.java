package com.practicaldime.jetty.gql.dao;

import java.util.ArrayList;
import java.util.List;

import com.practicaldime.jetty.gql.model.Link;

public class LinkRepositoryMem {

	private final List<Link> links;

    public LinkRepositoryMem() {
        links = new ArrayList<>();
        //add some links to start off with
        links.add(new Link("http://howtographql.com", "Your favorite GraphQL page", "test"));
        links.add(new Link("http://graphql.org/learn/", "The official docks", "test"));
    }

    public List<Link> getAllLinks() {
        return links;
    }
    
    public void saveLink(Link link) {
        links.add(link);
    }
}