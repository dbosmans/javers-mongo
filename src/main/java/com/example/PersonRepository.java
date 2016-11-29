package com.example;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by diederikbosmans on 29/11/16.
 */
public interface PersonRepository extends MongoRepository<Person, String> {

}
