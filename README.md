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

For secure Impala with the Hive2 driver:

```
java -Djava.security.auth.login.config=/path/to/jaas.conf \
  -Djavax.security.auth.useSubjectCredsOnly=false\
  -cp target/simple-client-tests*.jar com.cloudera.clienttest.ImpalaJDBCClientTest \
  <impala daemon host> <impalad principal> true]
```

An example JAAS config file for the Hive2 driver with a keytab:

```
com.sun.security.jgss.initiate {
 com.sun.security.auth.module.Krb5LoginModule required
 useKeyTab=true
 keyTab="/vagrant/vagrant.keytab"
 renewTGT=true
 doNotPrompt=true
 useTicketCache=true
 ticketCache="/tmp/app_cache"
 principal="vagrant@DEV"; 
};
```

For secure Impala with the Simba driver:

```
export JDBC_JARS=$(JARS=($(ls /path/to/simba/drivers/*.jar)); IFS=:; echo "${JARS[*]}")
java -Djava.security.auth.login.config=/path/to/jaas.conf \
  -cp ${JDBC_JARS}:simple-client-tests-1.0-SNAPSHOT.jar \
  com.cloudera.clienttest.ImpalaSimbaJDBCClientTest \
  <impala daemon host> "<query>" true
```

An example JAAS config file for the Simba driver with a keytab:

```
Client {
 com.sun.security.auth.module.Krb5LoginModule required
 useKeyTab=true
 keyTab="/vagrant/vagrant.keytab"
 renewTGT=true
 doNotPrompt=true
 useTicketCache=true
 ticketCache="/tmp/app_cache"
 principal="vagrant@DEV"; 
};
```