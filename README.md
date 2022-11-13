# Welcome to ssh-spring-boot-starter

## :fire: What is ssh-spring-boot-starter
ssh-spring-boot-starter is a java client for SSH designed for performance and ease of use.

## :bulb: Why choose ssh-spring-boot-starter
+ 🎯 **Easy to Use**: This is a spring-boot-starter project, just need add it to the maven pom.xml.
+ 🚀 **High Performance**: Use Apache Commons Pool2 to realize the pooling of session and realize the reusability of session.
+ 📈 **Monitor**: It can realize the control of the file upload rate and size, and realize the transmission process can be monitored

## 👣 Getting started

To get started with ssh-spring-boot-starter, first add it as a dependency in your Spring-boot project. 
If you're using Maven, that looks like this:

```xml
<dependency>
    <groupId>io.github.DarkAssassinator</groupId>
    <artifactId>ssh-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
If you're using Gradle, that looke like this:
```gradle
implementation group: 'io.github.DarkAssassinator', name: 'ssh-spring-boot-starter', version: '1.0.0'
```
And you should add ssh session configuration in your yml file:
```yml
ssh:
  session:
    # ACP session pool config
    pool:
      # The maximum number of active ssh session instances in the pool.
      maxTotal: 20
      # The maximum number of active ssh session of per ssh host in the pool.
      maxTotalPerKey: 10
      # The maximum number of sleeping instances of per ssh host in the pool.
      maxIdlePerKey: 4
      # When borrow an ssh session, whether to block waiting for a session when the pool is exhausted
      blockWhenExhausted: true
      # The maximum waiting time for the caller when the pool session is exhausted, it will be ignored if blockWhenExhausted is false
      maxWaitDuration: 500ms
      # The minimum time to allow idle session to live (or not be evicted)
      minEvictableIdleDuration: 60s
      # The interval at which the eviction thread pool scans the pool for free sessions
      durationBetweenEvictionRuns: 60s
      # Whether borrowObject performs abandoned session removal
      removeAbandonedOnBorrow: true
      # How long the object has been unused and returned after it was lent out and used for the last time is considered a leak
      removeAbandonedTimeoutDuration: 30s
    sftp:
      # enable upload monitor thread when upload files to remote host
      enableUploadMonitor: true
      # max upload rate (KB), if negative, will not limit
      maxUploadRate: 256
      # max file size (MB), if negative, will not limit
      maxFileSize: 100
```
Please check the :
|                       Parameter                        |  Default  |                                                                Desc                                                                 |
|-------------------------------------------------|-------|-----------------------------------------------------------------------------------------------------------------------------------|
| ssh.session.pool.maxTotal                       | 20    | The maximum number of active ssh session instances in the pool.                                                                   |
| ssh.session.pool.maxTotalPerKey                 | 10    | The maximum number of active ssh session of per ssh host & account in the pool.                                                   |
| ssh.session.pool.maxIdlePerKey                  | 4     | The maximum number of sleeping instances of per ssh host in the pool.                                                             |
| ssh.session.pool.blockWhenExhausted             | true  | When borrow an ssh session, whether to block waiting for a session when the pool is exhausted                                     |
| ssh.session.pool.maxWaitDuration                | 500ms | The maximum waiting time for the caller when the pool session is exhausted, it will be ignored if `blockWhenExhausted` is `false` |
| ssh.session.pool.minEvictableIdleDuration       | 60s   | The minimum time to allow idle session to live (or not be evicted)                                                                |
| ssh.session.pool.durationBetweenEvictionRuns    | 60s   | The interval at which the eviction thread pool scans the pool for free sessions                                                   |
| ssh.session.pool.removeAbandonedOnBorrow        | true  | Whether borrowObject performs abandoned session removal                                                                           |
| ssh.session.pool.removeAbandonedTimeoutDuration | 30s   | How long the object has been unused and returned after it was lent out and used for the last time is considered a leak            |
| ssh.session.sftp.enableUploadMonitor            | true  | enable upload monitor thread when upload files to remote host                                                                     |
| ssh.session.sftp.maxUploadRate                  | 256   | max upload rate (KB), if negative, will not limit                                                                                 |
| ssh.session.sftp.maxFileSize                    | 100   | max file size (MB), if negative, will not limit                                                                                   |

Next, you can instantiate a SSH Session pool like
```java
@Autowired
private SshSessionPool sessionPool;
```
Then you should create a SSH Session Host Entity named `SshSession`. See the example below:
```java
SshSession sshSession = new SshSession();
sshSession.setIp("xx.xx.xx.xx");
sshSession.setPort(22);
sshSession.setAccount("root");
sshSession.setPassword("this is a password");
```
Next, you can borrow a session holder from SessionPool.
```java
SshSessionHolder sessionHolder = null;
try {
   sessionHolder = sessionPool.getSessionHolder(sshSession);
} catch (Exception e) {
   log.error("cannot borrowObject ssh session holder", e);
} finally {
   sessionPool.returnSshSessionHolder(sshSession, sessionHolder);
}
```
How to execute shell command:
```java
sessionHolder.execCommand("echo 'hello world'");
```


