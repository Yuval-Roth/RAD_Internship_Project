package com.arealcompany.SpringData.repository;

import com.arealcompany.SpringData.business.records.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("games")
public interface GamesRepository extends MongoRepository<Game, String> {

}
