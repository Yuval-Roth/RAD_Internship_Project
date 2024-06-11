package com.arealcompany.ms_population.repository;

import com.arealcompany.ms_population.business.dtos.PopulationStat;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface PopulationRepository extends MongoRepository<PopulationStat, String> {

    @Aggregation("{ $match: { country: ?0 } }")
    PopulationStat findByCountry(String country);
}
