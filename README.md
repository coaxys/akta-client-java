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
```
Akta.init(apiKey, privateApiKey, url);
```
```
File file = new File("/path/to/file");
try {
    Akta.get().upload("your-uid", "your-projectname", file);
} catch (ConfigurationException e) {
    e.printStackTrace();
}
```
```
File file = new File("/path/to/file");
try {
    Akta.get().upload("your-uid", "your-projectname", "valid-slugified-arbo", file);
} catch (ConfigurationException e) {
    e.printStackTrace();
}
```
### License
This software is licensed under the Apache 2 license, quoted below.

Copyright 2015 Coaxys (http://www.coaxys.com).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.