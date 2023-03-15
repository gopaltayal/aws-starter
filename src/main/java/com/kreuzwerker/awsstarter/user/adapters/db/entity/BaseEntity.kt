package com.kreuzwerker.awsstarter.user.adapters.db.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    private lateinit var createdAt: LocalDateTime

    private lateinit var updatedAt: LocalDateTime

    @PreUpdate
    @PrePersist
    fun updateTimestamps() {
        if (!::createdAt.isInitialized) {
            createdAt = LocalDateTime.now()
        }
        updatedAt = LocalDateTime.now()
    }
}