package foo

import org.apache.commons.codec.digest.DigestUtils
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}


object Benchmark {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.master("local[2]").getOrCreate()
    val salt = "myextreme4567Sal!"
    /*
    1. Generate a file of random 15 character hex strings.
       Generated with: base64 -w15 /dev/urandom | head -n 100000000
    2. Read it with Spark and write it to a couple of Parquet files.
    3. Run the benchmark below.
    */

    val path = "/tmp/random_15char_hexes"

    val framer = spark.read.parquet(path)

    (1 to 20).foreach { c =>
      val li = time {
        framer.withColumn("imsi", tokenizeUdf(col("imsi"), lit(salt)))
      }

      val ne1 = time {
        framer.withColumn("imsi", altTokenization(col("imsi"), salt))
      }

      val ne4 = time {
        framer.withColumn("imsi", altTokenization2(col("imsi"), salt))
      }

      val ne7 = time {
        framer.withColumn("imsi", altTokenization3(col("imsi"), salt))
      }
    }

  }


  def time(block: => DataFrame): String = {
    val t0 = System.nanoTime()
    val result = block // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) + "ns")
    result.write.mode(SaveMode.Overwrite).parquet("/tmp/fob1")
    "x"
  }


  def hashSha256(id: String, salt: String): String = {
    DigestUtils.sha256Hex(id + salt)
  }

  def tokenizeUdf: UserDefinedFunction = udf { (id: String, salt: String) =>
    val res = Option(id) match {
      case Some("") => None
      case Some(i) => Some(hashSha256(i, salt))
      case _ => None
    }
    res
  }

  def altTokenization(c: Column, salt: String): Column = {
    val emptyImsiAsNull = when(c =!= lit(""), c)
    sha2(concat(emptyImsiAsNull, lit(salt)), 256)
  }

  def altTokenization2(c: Column, salt: String): Column = {
    val emptyImsiAsNull = when(c === lit(""), null).otherwise(c)
    sha2(concat(emptyImsiAsNull, lit(salt)), 256)
  }

  def altTokenization3(c: Column, salt: String): Column = {
    val emptyImsiAsNull = when(length(c) === 0, lit(null: String).cast(StringType)).otherwise(c)
    sha2(concat(emptyImsiAsNull, lit(salt)), 256)
  }
}
