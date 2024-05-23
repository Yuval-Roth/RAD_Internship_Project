package com.arealcompany.SpringData.repository;

import com.arealcompany.SpringData.business.records.Game;
import com.arealcompany.SpringData.business.records.Team;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface GamesRepository extends MongoRepository<Game, String> {

    @Aggregation("{ $limit : ?0 }")
    List<Game> findAllLimit(int maxCount);

    @Aggregation("{ $match : { season : ?0} }")
    List<Game> findBySeason(int season);

    @Aggregation(pipeline = {"{ $match : { season : ?0} }","{ $limit : ?1 }"})
    List<Game> findBySeasonLimit(int season, int maxCount);


}
