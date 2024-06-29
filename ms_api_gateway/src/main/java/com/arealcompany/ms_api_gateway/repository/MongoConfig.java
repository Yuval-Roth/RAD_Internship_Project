package com.arealcompany.ms_api_gateway.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.lang.NonNull;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final String DB_NAME = "user_db";
    private static final String MONGO_HOST = "mongodb";

    @Override
    @NonNull
    protected String getDatabaseName() {
        return DB_NAME;
    }

    @Override
    @NonNull
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + MONGO_HOST + ":27017/" +DB_NAME);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}
