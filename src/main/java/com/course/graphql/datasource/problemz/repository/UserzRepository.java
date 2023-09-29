package com.course.graphql.datasource.problemz.repository;

import com.course.graphql.datasource.problemz.entity.Userz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserzRepository extends CrudRepository<Userz, UUID> {
}
