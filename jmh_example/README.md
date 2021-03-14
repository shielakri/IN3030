# Using Java Microbenchmark Harness

### 1. Install Maven
Install Maven if not already installed. In Ubuntu you can do:
```bash
sudo apt install maven
```

### 2. Set up the project
Open your terminal, `cd` into the folder you would like to keep the project in, and paste this into the terminal:
```bash
mvn archetype:generate \
  -DinteractiveMode=false \
  -DarchetypeGroupId=org.openjdk.jmh \
  -DarchetypeArtifactId=jmh-java-benchmark-archetype \
  -DgroupId=org.sample \
  -DartifactId=test \
  -Dversion=1.0
```

This will generate a new JMH project in the `test` folder (the name is given in the second to last line).

### 3. Write the benchmarks
The benchmarks should be placed in the folder `test/src/main/java/org/sample/`. After you have performed the command in step 2, there will be a generated benchmark class called `MyBenchmark.java` in that folder. You can write your benchmarks in this class. With "benchmarks" I mean methods that have the annotation `@Benchmark`.

To easily test your code, just put your files (for example `SieveOfEratosthenes.java`) inside the `sample` folder, **and**, this is very important,  add one line at the top of your files that declares them as part of the package `org.sample` (the same package as the `MyBenchmark.java` file):

```Java
package org.sample;

class SieveOfEratosthenes {
  ...
}
```

Then you can reference them in the file `MyBenchmark.java`. For example:

```Java
@Benchmark
public void testPrime() {
  int n = 1000000;
  SieveOfEratosthenes soe = new SieveOfEratosthenes(n);
  soe.getPrimes();
}
```


### 4. Build the benchmarks
After you have written your benchmarks, `cd` into the `test` folder and build them with this command:
```bash
mvn clean verify
```

### 5. Run the benchmarks
After successully building the benchmarks, run them with this command:
```bash
java -jar target/benchmarks.jar
```

The benchmarks take some time. You can shorten the number of runs and modify the type of data you get by using annotations. I recommend the [Annotations section](https://javadevcentral.com/jmh-benchmark-with-examples#Annotations) in the first link shown under the section [Useful links](#Useful-links). Mark that all the annotations must be imported from the package `org.openjdk.jmh.annotations`.


# Combining with perf
Note: This profiler only works in Linux. The commands given below are for Ubuntu.

### Installing `perf`
This Linux profiler is found in `linux-common-tools` and can (in Ubuntu) be installed using the command:

```bash
sudo apt-get install linux-tools-$(uname -r) linux-tools-generic -y
```

The command is gotten from [this link](https://www.howtoforge.com/how-to-install-perf-performance-analysis-tool-on-ubuntu-20-04/). The command `uname -r` returns your current Kernel version.

Verify that the installation is finished by checking the `perf` version:

```bash
perf -v
```

The output should be something like this:

```bash
perf version 5.8.18
```

### Running the benchmarks with `perf`
The only thing you need to add is the `-prof` flag and the name of the profiler, which is `perf`:

```bash
java -jar target/benchmarks.jar -prof perf
```

#### Changing user rights
When trying to run your benchmark with the profiler `perf`, you might get an error that says that you do not have permission to collect stats. [Follow the answer here to fix that.](https://superuser.com/questions/980632/run-perf-without-root-rights)

### Useful links:
1. [A very useful introduction to JMH (I recommend this)](https://javadevcentral.com/jmh-benchmark-with-examples)
2. This blog post on JMH: [part 1](https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-1) [part 2](https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-2) [part3](https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-3)
4. [Java Microbenchmark Harness (JMH) GitHub](https://github.com/openjdk/jmh)
5. [JMH Presentation by Magnus Espeland](https://www.uio.no/studier/emner/matnat/ifi/IN3030/v20/lecture-material/uke05-2020/jmh.pdf)
