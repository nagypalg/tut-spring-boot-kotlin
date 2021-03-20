package com.example.blog

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class RepositoriesTests @Autowired constructor(
		val entityManager: TestEntityManager,
		val userRepository: UserRepository,
		val articleRepository: ArticleRepository) {

	@Test
	fun `When findByIdOrNull then return Article`() {
		val juergen = User("springjuergen", "Juergen", "Hoeller")
		entityManager.persist(juergen)
		val article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", juergen)
		entityManager.persist(article)
		entityManager.flush()
		val found = articleRepository.findByIdOrNull(article.id!!)
		assertThat(found).isEqualTo(article)
	}

	@Test
	fun `When findByLogin then return User`() {
		val juergen = User("springjuergen", "Juergen", "Hoeller")
		saveAndFlushUser(juergen)
		val user = userRepository.findByLogin(juergen.login)
		assertThat(user).isEqualTo(juergen)
	}

	private fun saveAndFlushUser(user: User) {
		entityManager.persist(user)
		entityManager.flush()
	}

	@Test
	fun `user's ID stays the same after persist`() {
		val juergen = User("springjuergen", "Juergen", "Hoeller")
		val gabor = User("springgabor", "Gabor", "Nagypal")
		juergen.id shouldBe null
		saveAndFlushUser(juergen)
		juergen.id shouldNotBe null
		gabor.id shouldBe null
		saveAndFlushUser(gabor)
		gabor.id shouldNotBe null
		val juergen2 = juergen.copy(firstname = "Juergen2")
		juergen shouldNotBe juergen2
		val user = userRepository.findById(juergen.id!!).orElseThrow()
		juergen shouldBe user
		juergen2 shouldNotBe user
		gabor shouldNotBe user
		juergen.firstname shouldBe "Juergen"
		entityManager.merge(juergen2)
		entityManager.flush()
		//this will change juergen!!!
		juergen shouldBe juergen2
		juergen.firstname shouldBe "Juergen2"
		val user2 = userRepository.findById(juergen.id!!).orElseThrow()
		juergen shouldBe user2
		juergen2 shouldBe user2
		gabor shouldNotBe user2
	}
}
