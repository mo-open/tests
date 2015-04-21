#---
# Excerpted from "Programming Concurrency on the JVM",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
#---
export AKKA_JARS="$AKKA_JARS:\
$AKKA_HOME/lib/akka/akka-remote-1.1.2.jar:\
$AKKA_HOME/lib/akka/protobuf-java-2.3.0.jar:\
$AKKA_HOME/lib/akka/netty-3.2.3.Final.jar:\
$AKKA_HOME/lib/akka/commons-io-2.0.1.jar"
javac -d . -classpath $AKKA_JARS *.java
java -classpath $AKKA_JARS com.agiledeveloper.pcj.Monitor
