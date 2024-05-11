package com.arealcompany.SpringData;

import com.arealcompany.SpringData.records.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NbaTeamsData extends MongoRepository<Team, String> {

}
