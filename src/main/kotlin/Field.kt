package com.zuhlke

data class Field(
  val x: Int,
  val y: Int,
) {
  val grid: Array<LongArray> by lazy {
    Array(x) { LongArray(y) }
  }
}
