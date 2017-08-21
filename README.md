akta-client-java
=======================================

![Coaxys logo](http://www.coaxys.com/public/images/coaxys-logo.svg)

Copyright (c) 2015 Coaxys <contact@coaxys.com>.
Licensed under the [Apache 2 licence](http://www.apache.org/licenses/LICENSE-2.0).

Description
-----------

Un client java pour [https://akta.coaxys.com](https://akta.coaxys.com). 

Utilisation
-----------
#### Init (mandatory)
```java
Akta.init(apiKey, privateApiKey, url);
```
#### Upload 
```java
File file = new File("/path/to/file");
try {
    Optional<AktaFile> optionalAktaFile = Akta.get().upload("your-uid", "your-projectname", file);
    if (optionalAktaFile.isPresent()) {
        AktaFile aktaFile = optionalAktaFile.get();
        // do something with your new AktaFile
    }
} catch (AktaException | ConfigurationException e) {
    e.printStackTrace();
}
```
```java
File file = new File("/path/to/file");
try {
    Optional<AktaFile> optionalAktaFile = Akta.get().upload("your-uid", "your-projectname", "valid/slugified-arbo", file);
    if (optionalAktaFile.isPresent()) {
        AktaFile aktaFile = optionalAktaFile.get();
        // do something with your new AktaFile
    }
} catch (AktaException | ConfigurationException e) {
    e.printStackTrace();
}
```
#### List of your projects 
```java
try {
    List<AktaProject> projects = Akta.get().projects("your-uid");
    for (AktaProject aktaProject : projects) {
        System.out.println(aktaProject);
    }
} catch (AktaException | ConfigurationException e) {
    e.printStackTrace();
}
```
#### List of a project root directories
```java
try {
    List<String> projectDirectories = Akta.get().projectDirectories("your-uid", "your-projectname");
    for (String directory : projectDirectories) {
        System.out.println(directory);
    }
} catch (AktaException | ConfigurationException e) {
    e.printStackTrace();
}
```
#### List of a project directory sub-directories
```java
try {
    List<String> projectDirectories = Akta.get().projectDirectories("your-uid", "your-projectname", "valid/slugified-arbo");
    for (String directory : projectDirectories) {
        System.out.println(directory);
    }
} catch (AktaException | ConfigurationException e) {
    e.printStackTrace();
}
```
#### List of all project files
```java
try {
    List<AktaFile> files = Akta.get().projectFiles("your-uid", "your-projectname");
    for (AktaFile aktaFile : files) {
        System.out.println(aktaFile);
    }
} catch (AktaException | ConfigurationException e) {
    e.printStackTrace();
}
```
#### List of all project directory files
```java
try {
    List<AktaFile> files = Akta.get().projectFiles("your-uid", "your-projectname", "valid/slugified-arbo");
    for (AktaFile aktaFile : files) {
        System.out.println(aktaFile);
    }
} catch (AktaException | ConfigurationException e) {
    e.printStackTrace();
}
```
### License
This software is licensed under the Apache 2 license, quoted below.

Copyright 2015 Coaxys (http://www.coaxys.com).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.