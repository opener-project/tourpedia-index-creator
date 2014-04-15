
# TOURPEDIA INDEX CREATOR

This repository contains the source code to create and query the tourpedia index. To manage the index, we use the <a href="http://lucene.apache.org/">Apache Lucene library</a>.

## Requirements

* Java 1.7 or newer

Development requirements:

* Maven

## Installation

Execute the *./INSTALL.sh* file:

        ./INSTALL.sh

## DataSet

In the *DataSet* folder we can find the tourpedia source *Places.csv* to create the tourpedia index.
The tourpedia source can be also downloaded from <a href="http://wafi.iit.cnr.it/openervm/outputs/Places.csv">here</a>.
            
## Usage

To execute the Domain Adaptation Tool execute this command:

```
    java -jar target/TourpediaIndex-0.0.1.jar [OPTIONS]
```
The *-h* or *--help* option will display the help information.

We have three functionalities: **kaf annotation**, **tourpedia index creation** and **queries**.

### KAF annotation

In order to annotate KAF files, please configure the *config/index.conf* file. This file configures the Tourpedia Index location, if especified index does not exist when linking entities within kaf files, a new index will be created. There are two keys:

* DATASET_PATH : path to the dataset.
* INDEX_PATH : index directory path.

Below you can see the options for KAF file annotation:

```
    Link entities within KAF file to the tourpedia index:
        -kaf [input.kaf output.kaf]
      If input and output KAF files are not especified,
      reads KAF from std input and writes in std output.
```

There is a test KAF file at *test/input.kaf* path, to annotate, you can use the following commands:


```
    java -jar target/TourpediaIndex-0.0.1.jar -kaf test/input.kaf test/output.kaf
```
or
```
    cat test/input.kaf java -jar target/TourpediaIndex-0.0.1.jar -kaf  > test/output.kaf
```

### Create the Tourpedia Index

Below you can see the options for tourpedia index creation:

```
    Create a new index:
        -create -doc docFile -index indexDir
      ARGUMENTS:
        -doc      docDir,       Path to the documents file.
        -index    indexDir,     Path to the index directory.
```
In order to index the tourpedia index, every text is lowercased and the whitespace characters are replaced by underscore characters.

### Queries

The top 100 URLs that match the query will be shown.
Below you can see the options to perform queries:

```
    Perform a query:
        -query -value value -index indexDir
      ARGUMENTS:
        -value    value,        The value to search.
        -index    indexDir,     Path to the index directory.
      OPTIONS:
        -exactMatch             Searches value with no wildcard.
        -showQuery              Prints performed query.
```
With the *showQuery* option, the string used to perform the query will be displayed, each query follows this patter: *Field:\*value\**. If we choose the *exatcMatch* option, the pattern is *Field:value*.

The queries are performed at NAME, DIRECTION and COUNTRY fields. So for every value (without *exactMatch* option), the query string is <i>NAME:\*value\* DIRECTION:\*value\* COUNTRY:\*value\*</i>. This will search any item containing the value at any field.

## Contributing

1. Pull it
2. Create your feature branch (`git checkout -b features/my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin features/my-new-feature`)
5. If you're confident, merge your changes into master.

