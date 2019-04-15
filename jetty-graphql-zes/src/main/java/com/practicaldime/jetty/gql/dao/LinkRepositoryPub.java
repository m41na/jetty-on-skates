package com.practicaldime.jetty.gql.dao;

import java.util.List;

import com.practicaldime.jetty.gql.listener.AppEvent;
import com.practicaldime.jetty.gql.model.Link;
import com.practicaldime.jetty.gql.model.LinkFilter;
import com.practicaldime.jetty.gql.websock.LinkCreated;

import io.reactivex.subjects.PublishSubject;

public class LinkRepositoryPub implements LinkRepository {

	private final LinkRepository repo;
	private final PublishSubject<AppEvent<LinkCreated>> subject;

	public LinkRepositoryPub(LinkRepository repo, PublishSubject<AppEvent<LinkCreated>> subject) {
		super();
		this.repo = repo;
		this.subject = subject;
		
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
		subject.onNext(new AppEvent<LinkCreated>("linkCreated", new LinkCreated(link.getId(), link.getUrl(), link.getUserId())));
	}
}
