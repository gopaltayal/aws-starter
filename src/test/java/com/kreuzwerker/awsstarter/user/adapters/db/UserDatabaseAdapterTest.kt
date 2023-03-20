package com.kreuzwerker.awsstarter.user.adapters.db

import com.kreuzwerker.awsstarter.user.adapters.db.entity.UserJpaEntity
import com.kreuzwerker.awsstarter.user.adapters.db.repositories.UserJpaRepository
import com.kreuzwerker.awsstarter.user.domain.models.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

// This annotation allows you to test your JPA Repository calls and the queries that are formed.
@DataJpaTest
// This allows you to test this with the In-Mem DB for faster testing
// But this test is not enough as the actual database will differ from H2
// You need to ensure to have E2E tests which use actual DB images for the same.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Without this you will be unable to configure @BeforeAll and @AfterAll methods.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class UserDatabaseAdapterTest {

    @Autowired
    lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Test
    fun `getUser returns single user when data user is found`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val userJpaEntity = UserJpaEntity(id, firstName, lastName, dateOfBirth)
        entityManager.persist(userJpaEntity)

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        val user = userDatabaseAdapter.getUser(id)

        Assertions.assertThat(user.userId).isEqualTo(id)
        Assertions.assertThat(user.firstName).isEqualTo(firstName)
        Assertions.assertThat(user.lastName).isEqualTo(lastName)
        Assertions.assertThat(user.dateOfBirth).isEqualTo(dateOfBirth)
    }

    @Test
    fun `getUser throws exception when user is not found`() {
        val id = "U123"

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        assertThrows<ResponseStatusException> { userDatabaseAdapter.getUser(id) }
    }

    @Test
    fun `getAllUsers returns list of users when data user is found`() {
        val id1 = "U123"
        val firstName1 = "Gopal"
        val lastName1 = "Tayal"
        val dateOfBirth1 = LocalDate.of(1992, 12, 21)
        val userJpaEntity1 = UserJpaEntity(id1, firstName1, lastName1, dateOfBirth1)
        val id2 = "U456"
        val firstName2 = "Angelica"
        val lastName2 = "Longetti"
        val dateOfBirth2 = LocalDate.of(1993, 9, 9)
        val userJpaEntity2 = UserJpaEntity(id2, firstName2, lastName2, dateOfBirth2)
        entityManager.persist(userJpaEntity1)
        entityManager.persist(userJpaEntity2)

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        val users = userDatabaseAdapter.getAllUsers()

        Assertions.assertThat(users.size).isEqualTo(2)
        Assertions.assertThat(users[0].userId).isEqualTo(id1)
        Assertions.assertThat(users[0].firstName).isEqualTo(firstName1)
        Assertions.assertThat(users[0].lastName).isEqualTo(lastName1)
        Assertions.assertThat(users[0].dateOfBirth).isEqualTo(dateOfBirth1)
        Assertions.assertThat(users[1].userId).isEqualTo(id2)
        Assertions.assertThat(users[1].firstName).isEqualTo(firstName2)
        Assertions.assertThat(users[1].lastName).isEqualTo(lastName2)
        Assertions.assertThat(users[1].dateOfBirth).isEqualTo(dateOfBirth2)
    }

    @Test
    fun `getAllUsers throws exception when users are not found`() {
        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        assertThrows<ResponseStatusException> { userDatabaseAdapter.getAllUsers() }
    }

    @Test
    fun `createUser creates a user`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        val savedUser = userDatabaseAdapter.createUser(user)

        Assertions.assertThat(savedUser.userId).isNotEqualTo(id)
        Assertions.assertThat(savedUser.firstName).isEqualTo(firstName)
        Assertions.assertThat(savedUser.lastName).isEqualTo(lastName)
        Assertions.assertThat(savedUser.dateOfBirth).isEqualTo(dateOfBirth)
    }

    @Test
    fun `createUser doesn't create multiple users with same name and date of birth`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val userJpaEntity = UserJpaEntity(id, firstName, lastName, dateOfBirth)
        val user = User(id, lastName, firstName, dateOfBirth)
        entityManager.persist(userJpaEntity)

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)

        assertThrows<ResponseStatusException> { userDatabaseAdapter.createUser(user) }
    }

    @Test
    fun `updateUser updates an existing user`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)
        val dbFirstName = "Angelica"
        val dbLastName = "Longetti"
        val dbDateOfBirth = LocalDate.of(1993, 9, 9)
        val dbUserJpaEntity = UserJpaEntity(id, dbFirstName, dbLastName, dbDateOfBirth)
        entityManager.persist(dbUserJpaEntity)

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        val updatedUser = userDatabaseAdapter.updateUser(user)

        Assertions.assertThat(updatedUser.userId).isEqualTo(id)
        Assertions.assertThat(updatedUser.firstName).isEqualTo(firstName)
        Assertions.assertThat(updatedUser.lastName).isEqualTo(lastName)
        Assertions.assertThat(updatedUser.dateOfBirth).isEqualTo(dateOfBirth)
    }

    @Test
    fun `updateUser throws exception when user is not found`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        assertThrows<ResponseStatusException> { userDatabaseAdapter.updateUser(user) }
    }

    @Test
    fun `delete user deletes an existing user`() {
        val id = "U123"
        val firstName = "Gopal"
        val lastName = "Tayal"
        val dateOfBirth = LocalDate.of(1992, 12, 21)
        val user = User(id, lastName, firstName, dateOfBirth)
        val userJpaEntity = UserJpaEntity(id, firstName, lastName, dateOfBirth)
        entityManager.persist(userJpaEntity)

        val userDatabaseAdapter = UserDatabaseAdapter(userJpaRepository)
        userDatabaseAdapter.deleteUser(user)

        assertThrows<ResponseStatusException> { userDatabaseAdapter.getUser(id) }
    }
}