package com.practicaldime.jetty.gql.dao;

import java.util.List;

import com.practicaldime.jetty.gql.listener.AppEventListener;
import com.practicaldime.jetty.gql.model.Link;
import com.practicaldime.jetty.gql.model.LinkFilter;
import com.practicaldime.jetty.gql.websock.LinkCreated;

public class LinkRepositoryPub implements LinkRepository {

	private final LinkRepository repo;
	private AppEventListener<LinkCreated> listener;

	public LinkRepositoryPub(LinkRepository repo, AppEventListener<LinkCreated> listener) {
		super();
		this.repo = repo;
		this.listener = listener;
	}

	@Override
	public Link findById(String id) {
		return repo.findById(id);
	}

	@Override
	public List<Link> getAllLinks() {
		return repo.getAllLinks();
	}

	@Override
	public List<Link> getAllLinks(LinkFilter filter, int skip, int limit) {
		return repo.getAllLinks(filter, skip, limit);
	}

	@Override
	public void saveLink(Link link) {
		repo.saveLink(link);		
		//publish event
		this.listener.onEvent(new LinkCreated(link.getUserId(), link.getUrl(), link.getDescription()));
	}
}
