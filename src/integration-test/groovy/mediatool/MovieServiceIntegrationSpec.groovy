package mediatool

import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
@TestFor(MovieService)
class MovieServiceIntegrationSpec extends Specification {

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
        movie.url == movieFromDb.url
        movie.title == movieFromDb.title
    }

    void "GetAllLocalMovies"() {
        given: "A url and a movieservice"
        String path = "./resources/test/movies"
        MovieService service = new MovieService()

        when: "You get all of the files in the url"
        List<Movie> movies = service.getAllLocalMovieInformation(path)

        then: "The list is greater than zero"
        movies.size() > 0
        movies.each { println it.title }
    }

}
