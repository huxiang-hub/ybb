package com.anaysis.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMongoConfig {
    protected String host;
    protected int port;
    protected String username;
    protected String password;
    protected String database;

    public MongoDbFactory mongoDbFactory() {
        ServerAddress serverAddress = new ServerAddress(host, port);
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(MongoCredential.createCredential(username, database, password.toCharArray()));
        return new SimpleMongoDbFactory(new MongoClient(serverAddress, credentialsList), database);
    }

    public abstract MongoTemplate getMongoTemplate();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
