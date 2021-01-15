package com.nubefact.ose.entity.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class PrimaryDatabaseConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryDatabaseConfiguration.class);

    @Value("${primary.mongodb.uri}")
    private String mongoUri;

    @Value("${primary.mongodb.database}")
    private String mongoDbName;

    @Primary
    @Bean
    public MongoTemplate mongoTemplate() {    
        LOGGER.debug(" Instantiating MongoDbFactory ");
        SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient(), mongoDbName);
            return new MongoTemplate(mongoDbFactory);
        }

    @Primary
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(mongoClientURI());
    }

    @Primary
    @Bean
    public MongoClientURI mongoClientURI() {
        LOGGER.debug(" creating connection with mongodb with uri [{}] ", mongoUri);
        return new MongoClientURI(mongoUri);
    }
}
