package com.example.blog

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Article(
    val title: String,
	val headline: String,
	val content: String,
    @ManyToOne val author: User,
	val slug: String = title.toSlug(),
	val addedAt: LocalDateTime = LocalDateTime.now(),
    @Id @GeneratedValue val id: Long? = null
)

@Entity
data class User(
	val login: String,
	val firstname: String,
	val lastname: String,
	val description: String? = null,
    @Id @GeneratedValue val id: Long? = null
)
