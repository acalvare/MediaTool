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
        Movie movie = new Movie(path: "/test/path", title:"test movie")

        when: "We save the movie and then retrieve it from the DB"
        movie.save()
        Movie movieFromDb = Movie.findByPath(movie.path)

        then: "The two movies should have the same path and title"
        movieFromDb != null
        movie.path == movieFromDb.path
        movie.title == movieFromDb.title
    }
}
