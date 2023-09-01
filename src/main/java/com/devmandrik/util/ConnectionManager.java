package com.devmandrik.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@UtilityClass
public class ConnectionManager {

    private final String PASSWORD_KEY = "db.password";
    private final String USERNAME_KEY = "db.username";
    private final String URL_KEY = "db.url";
    private final String POOL_SIZE_KEY = "db.pool.size";
    private final Integer DEFAULT_POOL_SIZE = 10;
    private BlockingQueue<Connection> pool;
    private List<Connection> sourceConnections;

    static {
        loadDriver();
        initConnectionPool();
    }

    private  void initConnectionPool() {
        var poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        var size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            var connection = open();
            var proxyConnection = (Connection)
                    Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? pool.add((Connection) proxy)
                                    : method.invoke(connection, args));
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }

    @SneakyThrows
    public Connection get() {
        return pool.take();
    }

    @SneakyThrows
    private Connection open() {
        return DriverManager.getConnection(
                PropertiesUtil.get(URL_KEY),
                PropertiesUtil.get(USERNAME_KEY),
                PropertiesUtil.get(PASSWORD_KEY)
        );
    }

    @SneakyThrows
    private void loadDriver() {
        Class.forName("org.postgresql.Driver");
    }

    @SneakyThrows
    public void closePool() {
        for (Connection sourceConnection : sourceConnections) {
            sourceConnection.close();
        }
    }
}
