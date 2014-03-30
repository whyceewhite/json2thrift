# thrifty json2thrift
JSON to Thrift Definition File Creator

## Purpose
The **thrifty json2thrift** utility parses json strings to generate a thrift definition file that represents the object structures found in all the json strings that were evaluated.

### What is JSON?
http://json.org

### What is Thrift?
http://thrift.apache.org

### Why is thrifty json2thrift useful?
The task of creating a thrift definition file is simple when evaluating one simple json object. However, what if the object is not fully representative of all the elements that may be contained with that json format? What if the json object is complex? If you are given dozens, hundreds or thousands of json data from which you need to determine the structs then this tool will be helpful. It will combine the various json objects so that elements are not omitted.

Thus, the power of the **thrifty json2thrift** utility is being able to evaluate *many* json files at once to best determine the expected structure of the json object. This is important because a json object may omit attributes if no value is present. 

The json strings below all contain the `id` element. However, the `name` definition is similar in the first two objects, yet completely omitted in the third one. In addition, the `dob` element that is present in the third element is omitted from the first two.

    {
      id : '19871231',
      name : {
        first : 'Benbo',
        last : 'Kinny',
        suffix : 'Jr'
      }
    }

    {
      id : '15571229',
      name : {
        first : 'Sadie',
        middle : 'Adalynn',
        last : 'Guzman'
      }
    }

    {
      id : '15571229',
      dob : '19900619'
    }


By evaluating the json object strings above, the **thrifty json2thrift** tool will surmise that the json definition has, at a minimum, the following structure:

    {
      dob : '',
      id : '',
      name : {
        first : '',
        middle : '',
        last : '',
        suffix : ''
      }
    }

And, as a result, will produce the following thrift definition:

    struct Name {
        1: optional string first;
        2: optional string middle;
        3: optional string last;
        4: optional string suffix;
    }
    struct Root {
        1: optional string dob;
        2: optional string id;
        3: optional Name name;
    }

## Installation
### System Requirements

* JDK 1.6+
* Maven 3.0.4+

### Building

The **thrifty json2thrift** utility runs as a command line program. Therefore, to quickly use the tool, clone this repository and run maven to build the library.

    git clone git@github.com:whyceewhite/json2thrift.git
    cd json2thrift
    mvn install
    bin/thrift-cli.sh <options>
    

## Usage
### Command Line Usage
To execute thrifty, run the thrifty jar on the command line with the following arguments:

    -h (--help)             : Displays the usage information.
    -i (--input-file) FILE  : A file containing JSON objects to derive a Thrift
                              struct definition. Or, a directory containing many
                              JSON files. Required.
    -n (--root-name) VAL    : The name of the root Thrift struct. If not provided
                              then the root struct name is defaulted to Root.
    -ns (--set-ns-all) VAL  : The namespace to apply to all supported languages.
                              Setting this value overrides the namespace setting
                              of any specific language.
    -o (--output-file) FILE : The file to which the derived Thrift struct is
                              written. If not provided then the struct is written
                              to standard out.
    --set-ns-cocoa VAL      : Indicates that the definition should include the
                              given namespace for the Cocoa language.
    --set-ns-cpp VAL        : Indicates that the definition should include the
                              given namespace for the CPP language.
    --set-ns-csharp VAL     : Indicates that the definition should include the
                              given namespace for the Csharp language.
    --set-ns-java VAL       : Indicates that the definition should include the
                              given namespace for the Java language.
    --set-ns-perl VAL       : Indicates that the definition should include the
                              given namespace for the Perl language.
    --set-ns-python VAL     : Indicates that the definition should include the
                              given namespace for the Python language.
    --set-ns-ruby VAL       : Indicates that the definition should include the
                              given namespace for the Ruby language.


The following subsections are examples with various options and will all refer, for demonstration purposes, to a json structure that looks like this:

    {
      id : '',
      name : {
        first : '',
        middle : '',
        last : '',
      }
    }


#### Example - Writing to an output file

The example below will read all files within the /tmp/thrifty/data directory and write the output to a file called /tmp/thrifty/out/run1.thrift.

    bin/thrifty-cli.sh -i /tmp/thrifty/data -o /tmp/thrifty/out/run1.thrift

#### Example - Specifying a root structure name

    bin/thrifty-cli.sh -i /tmp/thrifty/data/json1.json -n Person

Would yield the following structure:

    struct Name {
        1: optional string first;
        2: optional string middle;
        3: optional string last;
    }
    struct Person {
        1: optional string id;
        2: optional Name name;
    }

#### Example - Specifying a namespace

    bin/thrifty-cli.sh -i /tmp/thrifty/data/json1.json --set-ns-java org.test.thrift

Would yield the following structure:

    namespace java org.test.thrift
    
    struct Name {
        1: optional string first;
        2: optional string middle;
        3: optional string last;
    }
    struct Root {
        1: optional string id;
        2: optional Name name;
    }


## Release History
Current - 0.1.0-SNAPSHOT
