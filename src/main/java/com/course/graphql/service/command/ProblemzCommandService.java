package com.course.graphql.service.command;

import com.course.graphql.datasource.problemz.entity.Problemz;
import com.course.graphql.datasource.problemz.repository.ProblemzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProblemzCommandService {

    @Autowired
    private ProblemzRepository repository;

    public Problemz createProblemz(Problemz p) {
        var created = repository.save(p);

        return created;
    }
}
