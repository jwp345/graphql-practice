package com.course.graphql.service.command;

import com.course.graphql.datasource.problemz.entity.Solutionz;
import com.course.graphql.datasource.problemz.repository.SolutionzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SolutionzCommnadService {

    @Autowired
    private SolutionzRepository repository;

    public Solutionz createSolutionz(Solutionz s) {
        return repository.save(s);
    }

    public Optional<Solutionz> voteBad(UUID solutionId) {
        repository.addVoteBadCount(solutionId);
        var updated = repository.findById(solutionId);

        return updated;
    }

    public Optional<Solutionz> voteGood(UUID solutionId) {
        repository.addVoteGoodCount(solutionId);
        var updated = repository.findById(solutionId);

        return updated;
    }
}
