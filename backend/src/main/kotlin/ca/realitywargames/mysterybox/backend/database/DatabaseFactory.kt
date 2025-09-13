package ca.realitywargames.mysterybox.backend.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.HoconApplicationConfig
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

object DatabaseFactory {

    fun init(config: ApplicationConfig = HoconApplicationConfig(com.typesafe.config.ConfigFactory.load().resolve())) {
        val dataSource = createDataSource(config)
        runMigrations(dataSource)
        Database.connect(dataSource)
    }

    private fun createDataSource(config: ApplicationConfig): DataSource {
        val hikariConfig = HikariConfig().apply {
            driverClassName = config.property("database.driver").getString()
            jdbcUrl = config.propertyOrNull("database.url")?.getString()
                ?: config.property("database.default.url").getString()
            username = config.propertyOrNull("database.user")?.getString()
                ?: config.property("database.default.user").getString()
            password = config.propertyOrNull("database.password")?.getString()
                ?: config.property("database.default.password").getString()
            maximumPoolSize = 10
            minimumIdle = 5
            connectionTimeout = 30000
            idleTimeout = 600000
            maxLifetime = 1800000
        }
        return HikariDataSource(hikariConfig)
    }

    private fun runMigrations(dataSource: DataSource) {
        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migrations")
            .load()

        flyway.migrate()
    }
}
