package com.arealcompany.SpringData;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NbaTeamsData extends MongoRepository<Team, String> {

}
