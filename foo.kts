#!/usr/bin/env kotlin

import java.io.File

println("Hello Kotlin.")

val currentDirectory = File(".")
    .absolutePath
    .dropLast(1)
    .run { "Current directory: $this" }
    .let(::println)
