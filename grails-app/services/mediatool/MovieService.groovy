package mediatool

import groovy.json.JsonSlurper

import java.nio.file.Files

class MovieService {

    def apiKey="a3bc5a9763a2e77b25e0ce55f9869709"
    def baseUrl="https://api.themoviedb.org/3/search/"
    String baseImageURL = "http://image.tmdb.org/t/p/w"

    List<Files> getFiles(String path) {
        List<Files> files = new ArrayList<>()
        File directory = new File(path)
        directory.eachFile {
            files.add(it)
        }
        println "Found $files.size() files!"
        return files
    }

    List<Movie> sanitizeMovieTitles(List<Files> files){
        List<Movie> titles = new ArrayList<>()
        Range dates = 1900..2100
        def garbage = ['1080p','720p','mp4','avi','mkv']
        files.each{
            String sanitizedName = ""
            String path = it.absolutePath
            def name = path.split("\\\\")[-1]
            name = name.replaceAll("\\."," ")
            name = name.replaceAll("[!()\\]\\[\\{\\}\\-^%#@]"," ").replaceAll("  "," ")
            def words = name.split(" ")
            for(word in words) {
                if (word.isNumber()) {
                    if (!(word.toInteger() in dates)) {
                        sanitizedName += word + " "
                    } else
                        break
                }
                else if(!(word in garbage)){
                    sanitizedName += word + " "
                }else
                    break
            }
            titles.add(new Movie(title:sanitizedName.trim(),path:path))

        }
        return titles

    }

    List<Movie> getAllLocalMovieInformation(String path){
        List<Files> files = getFiles(path)
        List<Movie> titles = sanitizeMovieTitles(files)
        List<Movie> movies = new ArrayList<>()
        titles.each {
            Movie movie = getAdditonalMovieInformation(it)
            if(movie != null) {
                movie.save()
                movies << movie
            }
        }
        println "$movies.size() movies found!"
        return movies
    }


    Movie getAdditonalMovieInformation(Movie movie) {

        def response
        def slurper = new JsonSlurper()
        def type = "movie"
        def urlEncodedQuery = URLEncoder.encode(movie.title,"UTF-8")
        def url = "https://api.themoviedb.org/3/search/$type?api_key=$apiKey&language=en-US&query=$urlEncodedQuery&page=1"
        def httpConnection = new URL(url).openConnection()
        if (httpConnection.responseCode == httpConnection.HTTP_OK) {
            response = slurper.parse(httpConnection.inputStream.newReader())
            def result = response.results[0]
            if(result != null) {
                def artPath = result.poster_path
                byte[] picture = getPoster(artPath, 300)
                if (picture != null) {
                    movie.imageURL = baseImageURL + "300" + artPath
                    movie.title = result.original_title
                } else {
                    movie.title = result.original_title
                }
            }
            else{
                println "No movie data found for $movie.title"
            }
        }
         else if(httpConnection.responseCode == 429){
            println "Http Response for $movie.title resulted in $httpConnection.responseCode, sleeping for 10 seconds"
            sleep(10000)
        }
        else{
            println "Http Response for $movie.title resulted in $httpConnection.responseCode"
        }

        return movie
    }

    def getPoster(String path, int width){

        def httpConnection = new URL(baseImageURL+width+path).openConnection()
        if(httpConnection.responseCode == httpConnection.HTTP_OK) {
            Byte[] picture = httpConnection.inputStream.bytes
            return picture
        }
        return null
    }


}
