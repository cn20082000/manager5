package library.cndatabase

import java.sql.Connection
import java.sql.DriverManager

object CnDatabase {
    fun create(dbUrl: String, user: String, password: String, dbName: String): Boolean {
        cnDatabaseLog("Creating database \"$dbName\"...")
        val conn = try {
            DriverManager.getConnection(dbUrl, user, password)
        } catch (ex: Exception) {
            cnDatabaseLog(ex)
            cnDatabaseLog("Create database \"$dbName\" failed.")
            null
        } ?: return false

        try {
            if (checkExist(conn, dbName)) {
                cnDatabaseLog("Database \"$dbName\" existed.")
            } else {
                val statement = conn.createStatement()
                statement.executeUpdate("create database $dbName")
                cnDatabaseLog("Database \"$dbName\" created.")
            }
            return true
        } catch (ex: Exception) {
            cnDatabaseLog(ex)
            cnDatabaseLog("Create database \"$dbName\" failed.")
        }
        conn.close()
        return false
    }

    private fun checkExist(conn: Connection, dbName: String): Boolean {
        try {
            val resultSet = conn.metaData.catalogs

            while (resultSet.next()) {
                if (resultSet.getString(1).equals(dbName)) {
                    resultSet.close()
                    return true
                }
            }
            resultSet.close()
        } catch (ex: Exception) {
            cnDatabaseLog(ex)
        }
        return false
    }
}