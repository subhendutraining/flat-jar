package com.demo
import org.apache.spark.sql.SparkSession

object test extends App{

  println("Session is getting created")

  val spark = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate();

  println("Session creation completed")

}
