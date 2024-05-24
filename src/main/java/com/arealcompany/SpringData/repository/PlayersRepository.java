package com.arealcompany.SpringData.repository;

import com.arealcompany.SpringData.business.dtos.Game;
import com.arealcompany.SpringData.business.dtos.Player;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface PlayersRepository extends MongoRepository<Player, String> {

    @Aggregation("{ $limit : ?0 }")
    List<Player> findAllLimit(int maxCount);

}
