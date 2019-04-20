package com.practicaldime.jetty.gql.dao;

import java.util.List;

import com.practicaldime.jetty.gql.model.Link;
import com.practicaldime.jetty.gql.model.LinkFilter;

public interface LinkRepository {

	Link findById(String id);

	List<Link> getAllLinks();

	List<Link> getAllLinks(LinkFilter filter, int skip, int limit);

	void saveLink(Link link);

}