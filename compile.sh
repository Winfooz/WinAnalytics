#!/bin/bash
./gradlew --no-daemon -Dorg.gradle.debug=true :sample:clean :sample:compileDebugJavaWithJavac &
