package com.example;

import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaversMongoApplicationTests {


	@Autowired
	private PersonRepository repository;

	@Autowired
	private Javers javers;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private MongoTemplate mongoTemplate;



	@Test
	public void contextLoads() {
		Person p  = new Person();
		p.setName("test");

		Person saved = repository.save(p);
		Assert.assertNotNull(saved.getId());
		javers.commit("dbo", saved);
		saved.setName("tttttttt");
		javers.commit("dbo", saved);
        int size = javers.findChanges(QueryBuilder.byClass(Person.class).build()).size();
        System.out.printf("SIZE :" + size);

        Assert.assertTrue(1 == size);
	}

	@Test
	public void combineCriteria() {

		mongoOperations.indexOps(Person.class).ensureIndex(TextIndexDefinition.forAllFields());

		Person myFound  = new Person();
		myFound.setName("found");
		myFound.setFirstName("my");

		repository.save(myFound);

		Person myMissing  = new Person();
		myMissing.setName("missing");
		myMissing.setFirstName("my");

		repository.save(myMissing);

		Query query = Query.query(Criteria.where("firstName").is("my")).addCriteria(TextCriteria.forDefaultLanguage().matchingPhrase("found"));

		List<Person> people = mongoTemplate.find(query, Person.class);
		Assert.assertTrue(people.contains(myFound));
		Assert.assertFalse(people.contains(myMissing));
	}

}
