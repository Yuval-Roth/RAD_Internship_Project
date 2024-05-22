package com.arealcompany.SpringData.repository;

import com.arealcompany.SpringData.business.records.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("teams")
public interface Teams extends MongoRepository<Team, String> {

}
