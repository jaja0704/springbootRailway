package org.example.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.example.Sight;

@Repository
public interface SightRepository extends MongoRepository<Sight, String> {

    Sight[] findByZone(String zone);

}
