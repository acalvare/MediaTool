package mediatool

import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class MovieIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Saving a movie with an actor and then retrieving the movie works correctly"(){
        given: "A new movie and an mediatool.Actor"
        Actor actor = new Actor(firstName: "Brad", lastName: "Pitt")
        Movie movie = new Movie(url: "/test/url", title:"Troy")

        when: "We save the movie and then retrieve it from the DB"
        movie.addToActors(actor)
        movie.save()
        Movie movieFromDb = Movie.findByUrl(movie.url)


        then: "The two movies should have the same url and title"
        movieFromDb.actors.first().firstName == actor.firstName

    }
}
