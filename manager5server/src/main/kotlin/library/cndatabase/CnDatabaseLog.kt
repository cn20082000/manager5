package library.cndatabase

import library.cnlog.CnLog

private val log = CnLog("CnDatabase")

fun cnDatabaseLog(message: Any?) = log.print(message)

fun cnDatabaseLog() = log.print()