# Crypteron Java Proof-of-Concept Sample Applications

This is the Crypteron Java Proof-of-Concept (POC) Sample Applications repository to demonstrate usage of CipherDB, CipherObject and CipherStor in a quick and easy manner.

> NOTE: You need a valid AppSecret (API key) and unrestricted Java encryption strength for these samples. But don't worry, it's all explained below.

## Prerequisites

- A recent Java SE Development Kit 8
   - OpenJDK at http://openjdk.java.net/ OR via `apt-get install openjdk-8-jdk` on ubuntu 
   - Oracle's Java at http://www.oracle.com/technetwork/java/javase/downloads/index.html
   - If you have multiple versions and want to switch, try `sudo update-alternatives --config java`
- A recent version of Apache Maven 3.x: https://maven.apache.org/download.cgi or `apt-get install maven` on ubuntu

You may have to place your JDK and Maven bin directories in your path and set your `JAVA_HOME` environment variable to the root of your JDK install. Some installations with automatically do this for you.

## Getting Started

If you've received special instructions from Crypteron for specific scenarios, please follow those instructions. Otherwise for general audience, here is how you can get started.

1. Signup at [crypteron.com](https://www.crypteron.com/register)
2. Get the AppSecret off your [Crypteron Dashboard](https://my.crypteron.com/) and plug that back into whichever sample application's `src/main/resources/crypteron.properties` file that you're about to try out. i.e.
   - `./cipherdb-agent-sample-hibernate5/src/main/resources/crypteron.properties`
   - `./cipherobject-sample/src/main/resources/crypteron.properties`
   - `./cipherstor-sample/src/main/resources/crypteron.properties`
3. Run the samples by issuing the following commands from the repository root
   - Linux or macOS
     - CipherObject sample app: `./runSampleApp.sh cipherobject-sample`
     - CipherStor sample app: `./runSampleApp.sh cipherstor-sample`
     - CipherDB sample app: `./runSampleApp.sh cipherdb-agent-sample-hibernate5`
   - Windows (powershell)
     - CipherObject sample app: `.\runSampleApp.ps1 cipherobject-sample`
     - CipherStor sample app: `.\runSampleApp.ps1 cipherstor-sample`
     - CipherDB sample app: `.\runSampleApp.ps1 cipherdb-agent-sample-hibernate5`

The developer's documentation is [here](https://www.crypteron.com/docs) if you need it.
 
## Troubleshooting

- On older Oracle Java you may receive the error `java.security.InvalidKeyException: Illegal key size`. You can either update to a newer Oracle Java 8 version (Java [8u161 fixes this](https://www.oracle.com/technetwork/java/javase/8u161-relnotes-4021379.html#JDK-8170157)) or use [OpenJDK](https://adoptopenjdk.net/), since neither of them have this issue. If you cannot update, then install the Unlimited Strength Jurisdiction Policy JAR files available from http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html. 
   - There is a `README.txt` file within the downloaded `.zip` file that has instructions on how to install the Unlimited Strength files. If you have the JDK and JRE installed, you'll need to copy into the JRE (`$JAVA_HOME/jre/lib/security`) instead of the JDK (`$JAVA_HOME/lib/security`).
   - macOS users can find their installation at `/Library/Java/JavaVirtualMachines/<your_jdk_version>/Contents/Home/jre/lib/security`   

- If Crypteron complains about `Unable to parse the crypteron.appSecret`, make sure you've copied the AppSecret properly in Step 2 of the Getting Started section above
