package mediatool

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MovieService)
class MovieServiceSpec extends Specification {


    void "GetAllMovieFilesTest"() {
        given: "A path and a movieservice"
        String path = "\\\\HTPC\\hd"
        MovieService service = new MovieService()

        when: "You get all of the files in the path"
        List<File> files = service.getFiles(path)

        then: "The list is greater than zero"
        files.size() > 0
        files.each { println it }
    }

    void "GetAllMovieTitles"() {
        given: "A path and a movieservice"
        String path = "\\\\HTPC\\hd"
        MovieService service = new MovieService()

        when: "You get all of the files in the path"
        List<File> files = service.getFiles(path)
        List<Movie> titles = service.sanitizeMovieTitles(files)

        then: "The list is greater than zero"
        titles.size() > 0
        titles.each { println it.title }
    }

    void "GetMovieInformation"() {
        given: "A movieservice"
        MovieService service = new MovieService()

        when: "You get the movie list of the api call for 'Troy'"
        Movie movie = service.getAdditonalMovieInformation("Troy")

        then: "The result has at least one move"
        movie.title != null

    }

}
