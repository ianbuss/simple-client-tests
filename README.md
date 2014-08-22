# Building

```
mvn clean package
```

# Running

For Hive:

```
java -cp target/simple-client-tests*.jar com.cloudera.clienttest.HiveJDBCClientTest <HiveServer2 host> <user> [secure? {true|false}]
```

e.g.

```
java -cp target/simple-client-tests*.jar com.cloudera.clienttest.HiveJDBCClientTest n1.local user123 false
```

For Impala:

```
java -cp target/simple-client-tests*.jar com.cloudera.clienttest.ImpalaJDBCClientTest <impala daemon host> <user> [secure? {true|false}]
```
