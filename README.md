# DSBooks
A Java web app that demonstrates the use of the Data API with DSE and HCD. Should also work with DataStax Astra DB.

## Requirements
 - Java 21.
 - DSE 6.9 or HCD 1.0 _(with the Data API enabled)_.
 - Data API exposed on port 8181.
 - Environment variables set for:
 	 - `DB_APPLICATION_TOKEN`
 	 - `DB_API_ENDPOINT` set to Data API host + port 8181 (ex: `1.2.3.4:8181`)

### DSE/HCD Token
It's taken from the super user secret in the underlying Kubernetes project namespace. Can be obtained via cloud CLI and `kubectl`. Example with GKE:

```
$ gcloud container clusters get-credentials ui-playground --zone us-central1-c --project libraryproject \
&& kubectl get secret library-cluster-superuser --namespace aaronsproj -o yaml
```

This command returns the cluster's 64-bit encoded super user name and password. In the output from this command, look for the `data:` section:

```
data:
  password: fwhoefijldkghopinbvbnesd==
  username: osvhrnagirihoeprgjffjJPFJPIE==
```

The token is the word "Cassandra" followed by the base-64 encoding of the username and password, delimited by colons:

```
Cassandra:osvhrnagirihoeprgjffjJPFJPIE==:fwhoefijldkghopinbvbnesd==
```

## Schema
First, we will create a keyspace/namespace named `default_keyspace`. For that we will use a POST call to the Data API.

### Data API Headers
All Data API calls require the following header variables:
```
Accept:application/json
Content-Type:application/json
Token:Cassandra:osvhrnagirihoeprgjffjJPFJPIE==:fwhoefijldkghopinbvbnesd==
```

### Create a namespace
Make a POST call to the Data API with the following HTTP body:
```
http://1.2.3.4:8181/v1

{"createNamespace": 
  {"name": "default_keyspace",
   "options": {
      "replication": {
      "class": "NetworkTopologyStrategy",
      "yourdatacentername" : "3"
      }
    }
  }
```

### Create a collection
Make a POST call to the Data API with the following HTTP body:
```
http://1.2.3.4:8181/v1/default_keyspace

{"createCollection": 
  {"name": "library"}
}
```

### Insert a document
Make a POST call to the Data API with the following HTTP body:
```
http://1.2.3.4:8181/v1/default_keyspace/library

{ "insertOne":
  { "document": 
    { "title": "Code with Java 21",
      "author": "Aaron Ploetz",
      "image": "java_book_cover.png",
      "publisher": "BPB"
    }
  }
}
```

### Update a document
Get the document ID returned from the `insertOne` call. Make a POST call to the Data API with the following HTTP body:
```
http://1.2.3.4:8181/v1/default_keyspace/library

{ "updateOne":
  {
    "filter": 
    {
      "_id":
        { "$eq":"2c91e27a-4f48-4bdc-91e2-7a4f486bdcb8"}
    },
    "update": {
      "$set": {
          "isbn": "978-9355519993",
          "amazon_link": "https://www.amazon.com/dp/B0CS3KZTQ9"
        }
      }
  }
}
```

### Insert many documents
Make a POST call to the Data API with the following HTTP body:
```
http://1.2.3.4:8181/v1/default_keyspace/library

{ "insertMany":
  { "documents": [
      { "title": "Seven NoSQL Databases in a Week",
        "author": "Aaron Ploetz, Brian Wu, Devram Kandahare, and Sudarshan Kadambi",
        "publisher": "Packt",
        "isbn": "978-1787288867",
        "image": "sevenNoSQLDBs.jpg",
        "amazon_link": "https://www.amazon.com/Seven-NoSQL-Databases-Week-functionalities/dp/1787288862/"
      },
      { "title": "Mastering Apache Cassandra",
        "author": "Aaron Ploetz and Tejaswi Malepati",
        "publisher": "Packt",
        "isbn": "978-1789131499",
        "image": "masteringApacheCassandra.jpg",
        "amazon_link": "https://www.amazon.com/Mastering-Apache-Cassandra-3-x-availability/dp/1789131499/"
      },
      { "title": "Cassandra the Definitive Guide",
        "author": "Eben Hewitt and Jeff Carpenter",
        "publisher": "O'Reilly",
        "isbn": "978-1492097143",
        "image": "cassandra_definitive.jpg",
        "amazon_link": "https://www.amazon.com/Cassandra-Definitive-Guide-Revised-Distributed/dp/1492097144/"
      },
      { "title": "Managing Cloud Native Data on Kubernetes",
        "author": "Jeff Carpenter and Patrick McFadin",
        "publisher": "O'Reilly",
        "isbn": "978-1098111397",
        "image": "cndb_kubernetes.jpg",
        "amazon_link": "https://www.amazon.com/Managing-Cloud-Native-Data-Kubernetes/dp/1098111397/"
      },
      { "title": "The Practitioner's Guide to Graph Data",
        "author": "Denise Gosnell, Matthias Broecheler",
        "publisher": "O'Reilly",
        "isbn": "978-1492044079",
        "image": "denises_book.png",
        "amazon_link": "https://www.amazon.com/Practitioners-Guide-Graph-Data/dp/1492044075/"
      }
    ]
  }
}
```

## Build
```
mvn clean install
```

## Run
```
java -jar target/dsbooks-0.0.1-SNAPSHOT.jar
```