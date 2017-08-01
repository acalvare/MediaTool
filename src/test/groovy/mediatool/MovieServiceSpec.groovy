package mediatool

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MovieService)
class MovieServiceSpec extends Specification {


    void "Gets Params Maps"(){
        given: "A movie service"
        MovieService service = new MovieService()

        when: "I generate the params map"
        Map paramsMap = service.generateParmsMap()

        then: "The params map is not empty or null"
        paramsMap != null
        paramsMap.keySet().size() > 0
    }

    void "Getting a new session id"(){
        given: "A movie service"
        MovieService service = new MovieService()

        when: "I generate attempt to get a new session id"
        String id= service.getSessionCookie("https://www.allflicks.net")

        then: "The session id is non null and non empty"
        println "Session id is $id"
        id != null
        id.length() > 0
    }

    void "GetAllMovieFilesTest"() {
        given: "A url and a movieservice"
        String path = "./resources/test/movies"
        MovieService service = new MovieService()

        when: "You get all of the files in the url"
        List<File> files = service.getFiles(path)

        then: "The list is greater than zero"
        files.size() > 0
        files.each { println it }
    }

    void "GetAllMovieTitles"() {
        given: "A url and a movieservice"
        String path = "./resources/test/movies"
        MovieService service = new MovieService()

        when: "You get all of the files in the url"
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
        Movie movie = service.getAdditonalMovieInformation(new Movie(title: "Troy"),0)

        then: "The result has at least one move"
        movie.title != null

    }

}
