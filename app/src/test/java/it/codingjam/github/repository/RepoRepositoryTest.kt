package it.codingjam.github.repository

import assertk.assert
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.nhaarman.mockito_kotlin.mock
import it.codingjam.github.api.GithubService
import it.codingjam.github.util.TestData.REPO_1
import it.codingjam.github.util.TestData.REPO_2
import it.codingjam.github.util.willReturnJust
import okhttp3.Headers
import org.junit.Test
import retrofit2.Response

class RepoRepositoryTest {

    val githubService: GithubService = mock()

    val repository = RepoRepository(githubService)

    @Test fun search() {
        val header = "<https://api.github.com/search/repositories?q=foo&page=2>; rel=\"next\"," +
                " <https://api.github.com/search/repositories?q=foo&page=34>; rel=\"last\""
        val headers = mapOf("link" to header)

        githubService.searchRepos(QUERY) willReturnJust
                Response.success(listOf(REPO_1, REPO_2), Headers.of(headers))

        val (items, nextPage) = repository.search(QUERY).blockingGet()

        assert(items).containsExactly(REPO_1, REPO_2)
        assert(nextPage).isEqualTo(2)
    }

    companion object {
        private const val QUERY = "abc"
    }
}