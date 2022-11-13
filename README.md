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


