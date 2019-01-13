package com.practicaldime.jetty.gql.dao;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.practicaldime.jetty.gql.model.Link;
import com.practicaldime.jetty.gql.model.User;
import com.practicaldime.jetty.gql.model.Vote;

public class VoteResolver implements GraphQLResolver<Vote> {
    
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    public VoteResolver(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public User user(Vote vote) {
        return userRepository.findById(vote.getUserId());
    }
    
    public Link link(Vote vote) {
        return linkRepository.findById(vote.getLinkId());
    }
}