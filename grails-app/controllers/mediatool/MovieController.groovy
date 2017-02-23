package mediatool

class MovieController {
    MovieService service = new MovieService()

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
        List<Movie> movieList = service.getAllLocalMovieInformation("\\\\HTPC\\hd")
        print "Generating movie view"
        [movies:movieList]
        /*[movie:movieList[0]]*/
    }
}
