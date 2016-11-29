package com.example;

import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaversMongoApplicationTests {


	@Autowired
	PersonRepository repository;

	@Autowired
    Javers javers;

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

}
