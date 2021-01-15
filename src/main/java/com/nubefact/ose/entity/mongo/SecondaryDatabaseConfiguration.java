package com.nubefact.ose.entity.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class SecondaryDatabaseConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecondaryDatabaseConfiguration.class);

    @Value("${secondary.mongodb.uri}")
    private String mongoUri;

    @Value("${secondary.mongodb.database}")
    private String mongoDbName;

    @Bean(name = "mongoTemplateSecond")
    public MongoTemplate mongoTemplate() {    
        LOGGER.debug(" Instantiating MongoDbFactory ");
        SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientSeconday(), mongoDbName);
            return new MongoTemplate(mongoDbFactory);
        }

    @Bean
    public MongoClient mongoClientSeconday() {
        return new MongoClient(mongoClientURISeconday());
    }

    @Bean
    public MongoClientURI mongoClientURISeconday() {
        LOGGER.debug(" creating connection with mongodb with uri [{}] ", mongoUri);
        return new MongoClientURI(mongoUri);
    }
}
