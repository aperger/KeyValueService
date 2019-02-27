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

### Install JDK and Gradle on Ubuntu

```shell
sudo apt-get install default-jre
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
