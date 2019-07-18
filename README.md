# Scala UDF performance vs spark.sql.functions

A benchmark between an implementation of a hashing algorithm written using a Spark UserDefinedFunction and its equivalent using not but functionality provided through `org.apache.spark.sql.functions`.

The results indicate that after a warm-up time, the performance is similar.

