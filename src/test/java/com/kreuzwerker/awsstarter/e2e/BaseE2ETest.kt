package com.kreuzwerker.awsstarter.e2e

import com.fasterxml.jackson.databind.ObjectMapper
import com.kreuzwerker.awsstarter.user.adapters.db.repositories.UserJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.MySQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
open class BaseE2ETest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userJpaRepository: UserJpaRepository


    @BeforeEach
    fun cleanUp() {
        userJpaRepository.deleteAll()
    }

    // Used to provide the same MySQL Container for all E2E tests
    // Better than pulling image for each test
    companion object {
        // FIXME : The test image is hardcoded?
        private val mysql: MySQLContainer<*> =
            MySQLContainer(DockerImageName.parse("mysql:8.0.32").asCompatibleSubstituteFor("mysql:8.0.32"))
                .withDatabaseName("aws-starter")
                .withPassword("aws-starter-pass")

        // Represents a JAVA based static block
        // This will further provide additional properties to the Test Container based test
        // Also start a single container for all the tests
        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            mysql.start()

            registry.add("spring.liquibase.enabled") { true }
            registry.add("spring.datasource.driver-class-name") { "com.mysql.cj.jdbc.Driver" }
            registry.add("spring.datasource.url") { mysql.jdbcUrl + "?serverTimezone=Europe/Rome&allowPublicKeyRetrieval=true&useSSL=false" }
            registry.add("spring.datasource.username", mysql::getUsername)
            registry.add("spring.datasource.password", mysql::getPassword)
        }
    }
}