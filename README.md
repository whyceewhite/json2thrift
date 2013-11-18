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
    ]

## Installation
tbd

## Usage
tbd

## Release History
Current - 0.1.0-SNAPSHOT
