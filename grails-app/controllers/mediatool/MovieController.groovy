package mediatool

class MovieController {
    MovieService movieService
    static defaultAction = "list"

    def index() {
        List<Movie> movies = [new Movie(title: "Gone with the Wind"), new Movie(title: "Troy")]
        render "<ul>"
        movies.each{
            render "<li> $it.title </li>"
        }
        render "</ul>"
        log.info "This is a test log"
    }

    def list(){
        List<Movie> movieList = movieService.getAllLocalMovieInformation("./resources/test/movies")
        print "Generating movie view"
        [movies:movieList]
    }
}
