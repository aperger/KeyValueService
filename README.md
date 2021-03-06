# KeyValueService
KeyValue Server and Client solution - (OneIdentity Test) 

## Directory structure
The server application is in `server` directory, the client application is in ` client` directory. The common parts, shared classes are in the `common` subdirectory. Both applications refer to the commons directory (depend on it).
The Python based client application is in `pyclient`.

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

## Start Server and Client (Java)

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
\send DATA01 rth e6 984te tp94694u983 5694
Response #1:	
DONE : size is 1
\send DATA02 iö9 4w tutu 3öurö934u5ruju4wrö jfmvlkjtr931u4
Response #2:	
DONE : size is 2
\get
Response #3:	
DATA01 rth e6 984te tp94694u983 5694
DATA02 iö9 4w tutu 3öurö934u5ruju4wrö jfmvlkjtr931u4

\get DATA02
Response #4:	
DATA02 iö9 4w tutu 3öurö934u5ruju4wrö jfmvlkjtr931u4

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
Content of `/etc/profile.d/gradle.sh` file:
```bash
export GRADLE_HOME=/opt/gradle/gradle-4.10.3
export PATH=${GRADLE_HOME}/bin:${PATH}
```

```shell
sudo mcedit /etc/profile.d/gradle.sh
sudo chmod +x /etc/profile.d/gradle.sh
source /etc/profile.d/gradle.sh 
gradle -v
```

## Start Python client

```bash
$ pwd
/home/aperger/Data/KeyValueService/pyclient/src
$ export PYTHONPATH=.:$PATHONPATH
$ python3 com/oneidentity/Client.py 
usage: Client.py [-h] host port
Client.py: error: the following arguments are required: host, port
$ python3 com/oneidentity/Client.py localhost 11111
Connecting to server 'localhost' on port :11111
> \get
DATA01 oir98eut oejt u49 

> \quit
```

## Supported commands / request types

Put/Send data to server:
```
\send [Key] [value]
```

Get back all data form server
```
\get
```

Get back one value from server
```
\get [Key]
```

Quit
```
\quit
```

## Build with 'Makefile'

`TODO`


## Stress Test
The client application (Java) can read the standart input. So we can create huge test files with predefined requests (commands) to send lots of data into the server.

On Windows:
```
D:\PrivateWork\Java\OneIdentity\client\build\distributions>keyvalue-client\bin\keyvalue-client localhost 11111 < ..\..\input_big01.txt
```

On Linux:
```shell
$ pwd
/home/aperger/Data/KeyValueService/client/build/distributio
$ cat ../../input_big01.txt | ./keyvalue-client/bin/keyvalue-client localhost 11111

-- OR ---

$ ./keyvalue-client/bin/keyvalue-client localhost 11111 < ../../input_big01.txt
```

We can start multiple client instances to send data into the server or query it.
