#WatchExample
Watch on Namespaces/Projects. List Annotations and Labels when added, modified or deleted. Need cluster_reader!

#PVCExample
Watch the size of all PVCs of a specified Namespace(default:default). Set the Namespace with environment: K8S_NAMESPACE

# Running examples

```sh


mvn package
mvn exec:java -Dexec.mainClass="WatchExample"

or

mvn exec:java -Dexec.mainClass="PVCExample"
```

