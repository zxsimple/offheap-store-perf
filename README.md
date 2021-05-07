# offheap-store-perf
Peformance benchmark for Java off-heap storage

### Build the project
```shell
mvn clean compile assembly:single
```

### Performance benchmark test
**Write 2 billion items into MapDB**

```shell
export JAVA_HOME=$(pwd)/jdk-11.0.11+9
export PATH=$PATH:$JAVA_HOME/bin
java -Djava.io.tmpdir=/mnt/perf/data -cp offheap-store-perf-1.0.0-jar-with-dependencies.jar com.zxsimple.perf.MapDbMMapCache w 2000000000
```

**Read 2 billion items from MapDB**
```shell
export JAVA_HOME=$(pwd)/jdk-11.0.11+9
export PATH=$PATH:$JAVA_HOME/bin
java -Djava.io.tmpdir=/mnt/perf/data -cp offheap-store-perf-1.0.0-jar-with-dependencies.jar com.zxsimple.perf.MapDbMMapCache r 2000000000
```

**Write 2 billion items into ChronicleMap**
```shell
export JAVA_HOME=$(pwd)/jdk-11.0.11+9
export PATH=$PATH:$JAVA_HOME/bin
java -Djava.io.tmpdir=/mnt/perf/data -cp offheap-store-perf-1.0.0-jar-with-dependencies.jar com.zxsimple.perf.ChronicleMapCache w 2000000000
```

**Read 2 billion items from ChronicleMap**
```shell
export JAVA_HOME=$(pwd)/jdk-11.0.11+9
export PATH=$PATH:$JAVA_HOME/bin
java -Djava.io.tmpdir=/mnt/perf/data -cp offheap-store-perf-1.0.0-jar-with-dependencies.jar com.zxsimple.perf.ChronicleMapCache r 2000000000
```

### Test result

**高IO - SAS**

> IOPS上限1,800，IOPS突发上限5,000

|Storage(R/W)     |10K(ns)      |100K(ns)      |1M(ns) |10M(ns) |50M(ns) |
| ---- | ---- | ---- | ---- | ---- | ---- | ---- |
| MapDB(W) | 37584770 | 253219604 | 1905269137 | 31101750491 |  |
| MapDB(R) | 35282173 | 278026460 | 1829999883 | 25191501020 |  |
| ChronicleMap(W) | 181770196 | 576221796 | 3644390396 | 48709053791 | 355514017130 |
| ChronicleMap(R) | 95215281 | 347267463 | 1959736493 | 18450060515 | 89740821302 |

**通用型SSD - GPSSD**

> IOPS上限2,300，IOPS突发上限8,000

| Storage(R/W)    | 10K(ns)   | 100K(ns)  | 1M(ns)     | 10M(ns)     | 50M(ns)      | 
| --------------- | --------- | --------- | ---------- | ----------- | ------------ | 
| MapDB(W)        | 40376008  | 255707438 | 1876907352 | 31692810149 |              |
| MapDB(R)        | 27448796  | 226203656 | 1848050799 | 26478899168 |              | 
| ChronicleMap(W) | 230567163 | 585120010 | 3983323047 | 37181110409 | 239614024885 |
| ChronicleMap(R) | 107263033 | 351693637 | 2072631422 | 19186194044 | 91854562988  |

**超高IO - SSD**

> IOPS上限6,500，IOPS突发上限16,000

| Storage(R/W)    | 10K(ns)   | 100K(ns)  | 1M(ns)     | 10M(ns)     | 50M(ns)      | 1B(ns) |
| --------------- | --------- | --------- | ---------- | ----------- | ------------ | -------- |
| MapDB(W)        | 24414392  | 241636276 | 1710527536 | 25158254950 |              |          |
| MapDB(R)        | 18135518  | 190535648 | 1726740772 | 24883570860 |              |          |
| ChronicleMap(W) | 106815321 | 436364794 | 3855152122 | 38087833316 | 206487973496 |          |
| ChronicleMap(R) | 42593050  | 275962770 | 2093011499 | 20171377215 | 91046667947  |          |

**Write ops**

<img src=".\images\write-ops.png" alt="write-ops" style="zoom:67%;" />

**Read ops**

<img src=".\images\read-ops.png" alt="read-ops" style="zoom:67%;" />

**ChronicleMap write ops by storage**

<img src=".\images\chroniclemap-write-ops-by-storage.png" alt="chroniclemap-write-ops-by-storage" style="zoom:67%;" />

**ChronicleMap read ops by storage**

<img src=".\images\chroniclemap-read-ops-by-storage.png" alt="chroniclemap-read-ops-by-storage" style="zoom:67%;" />

**MapDB write ops by storage**

<img src=".\images\mapdb-write-ops-by-storage.png" alt="mapdb-write-ops-by-storage" style="zoom: 67%;" />

**MapDB read ops by storage**

<img src=".\images\mapdb-read-ops-by-storage.png" alt="read-ops" style="zoom:67%;" />

**多进程读ChronicleMap**

| Process # | Time(ns)    | OPS/s       |
| --------- | ----------- | ----------- |
| #1        | 92398998134 | 1082262.817 |
| #2        | 92972819425 | 1075583.172 |
| #3        | 92739834858 | 1078285.293 |
| #4        | 93588185250 | 1068510.942 |

**R/W 1 billion data**

> Storage type: SSD

| Time(ns) | OPS/s |
| -------- | ----- |
|          |       |
