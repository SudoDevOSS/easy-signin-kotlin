# Easy SignIn Kotlin

## Overview

This Project is an open-source project written in Kotlin

The goal of this project is apply the clean architecture on a module and include basic API operations such as

- Authentication
    - Login
    - Register
    - Reset Password
- Account
    - Fetch Account Info
    - Update Account Info

## Dependencies

- [OkHTTP](https://square.github.io/okhttp/)
    - Logging
    - Bom
- [Moshi](https://github.com/square/moshi)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)

## Tests

Unit tests written using [Junit5](https://junit.org/junit5/docs/current/user-guide/), [Mockk](https://mockk.io/)
& [OkHttp Mock Server](https://square.github.io/okhttp/#mockwebserver)

## Getting Started

### Requirements

- Java 1.8
- Kotlin 1.5.0
- Gradle 6.2

### Usage
- Add jar file to your project
- Initialize library using
``` java
val rootContainer = DI {
  importAll(DIConfigurator.getDIModules())
}

BaseNetworking.configureClient("URL", "AUTHENTICATION_TOKEN")
```
---
## License
```
MIT License

Copyright (c) 2021 sudo-dev-oss

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```