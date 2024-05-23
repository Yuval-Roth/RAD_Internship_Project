package com.arealcompany.SpringData.repository;

import com.arealcompany.SpringData.business.records.Team;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface TeamsRepository extends MongoRepository<Team, String> {

    @Aggregation("{ $limit : ?0 }")
    List<Team> findAllLimit(int maxCount);
}
