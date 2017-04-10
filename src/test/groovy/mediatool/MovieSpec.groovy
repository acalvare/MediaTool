package mediatool

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Movie)
class MovieSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Saving a movie and then retrieving it works"(){
        given: "A new movie"
        Movie movie = new Movie(url: "/test/url", title:"test movie")

        when: "We save the movie and then retrieve it from the DB"
        movie.save()
        Movie movieFromDb = Movie.findByUrl(movie.url)

        then: "The two movies should have the same url and title"
        movieFromDb != null
        movie.url == movieFromDb.url
        movie.title == movieFromDb.title
    }
}
