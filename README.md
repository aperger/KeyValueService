# KeyValueService
KeyValue Server and Client solution - (OneIdentity Test) 

## Directory structure
The server application is in `server` directory, the client application is in ` client` directory. The common parts, shared classes are in the `common` subdirectory. Both applications refer to the commons directory (depend on it).

## Build with 'gradle'
When we get the source of applications (downloading or using "git clone"), we can use the `gradle build` command to build them. Example (the server):

```shell
cd server
gradle build
```

Both applications are build with the `application gradle-plugin`. This pulgin provides distributable and compressed binaries: both ZIP and TAR. If we enter into `server/build/distributions` or `client/build/distributions` directory after succesfull build, where the files are (`keyvalue-server.tar ` or `keyvalue-client.zip `), we can extract them:

```shell
cd server/build/distributions
tar xvf keyvalue-client.tar

```  

We will get directory with the previous commands called `keyvalue-client` or `keyvalue-server`. Inside these directories there will be `bin` and `lib` subdirectories. The `lib` directory contains the JAR files, the `bin` directory contains the executable files (`keyvalue-client` or `keyvalue-server.bat`). With these scripts we can executes the programs in command line under Windows and Linux too.

## Start Server and Client

Server:

```shell
$ pwd
/home/aperger/Data/KeyValueService/server/build/distributions
$ tar xvf keyvalue-server.tar
keyvalue-server/
keyvalue-server/lib/
keyvalue-server/lib/keyvalue-server.jar
keyvalue-server/lib/keyvalue-common.jar
keyvalue-server/bin/
keyvalue-server/bin/keyvalue-server.bat
keyvalue-server/bin/keyvalue-server
$ cd keyvalue-server/bin/
$ ./keyvalue-server
Usage: java -cp=[...] Server <port number>
-- OR --
Usage: <path to keyvalue-server>/bin/keyvalue-server <port number>
$ ./keyvalue-server 11111
IP1. = fe80:0:0:0:3357:d6b3:ffe9:172b%enp0s3 is a linked address
IP2. = 10.0.2.15
IP3. = 0:0:0:0:0:0:0:1%lo
IP4. = 127.0.0.1
com.oneidentity.net.TcpServer is bound on 11111
Close a client sokcet: 1
Close a client sokcet: 2
```

Client:

```shell
$ pwd
/home/aperger/Data/KeyValueService/client/build/distributions
$ tar xvf keyvalue-client.tar 
keyvalue-client/
keyvalue-client/lib/
keyvalue-client/lib/keyvalue-client.jar
keyvalue-client/lib/keyvalue-common.jar
keyvalue-client/bin/
keyvalue-client/bin/keyvalue-client.bat
keyvalue-client/bin/keyvalue-client
$ cd keyvalue-client/bin/
$ ./keyvalue-client
Usage: java -cp=[...] Client <host name> <port number>
-- OR --
Usage: <path to keyvalue-client>/bin/keyvalue-client <host name> <port number>
$ ./keyvalue-client localhost 11111
\send ID001 data01
Response #1:	
DONE : size is 1
\get ID001
Response #2:	
ID001 data01

\quit
********** E N D *********
```

### Install JDK and Gradle on Ubuntu

```shell
sudo apt-get install default-jdk
java -version
cd temp/
wget https://downloads.gradle.org/distributions/gradle-4.10.3-bin.zip
sudo unzip -d /opt/gradle gradle-4.10.3-bin.zip 
ls /opt/gradle/gradle-4.10.3/
sudo vi /etc/profile.d/gradle.sh
```
Cotent of `/etc/profile.d/gradle.sh` file:
```bash
export GRADLE_HOME=/opt/gradle/gradle-4.10.3
export PATH=${GRADLE_HOME}/bin:${PATH}
```

```shell
sudo mcedit /etc/profile.d/gradle.sh
sudo mc
sudo chmod +x /etc/profile.d/gradle.sh
source /etc/profile.d/gradle.sh 
gradle -v

```

## Build with 'Makefile'

`TODO`
