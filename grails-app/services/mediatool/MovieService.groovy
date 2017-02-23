package mediatool

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import grails.transaction.Transactional
import groovy.json.JsonSlurper

import java.nio.file.Files
import java.nio.file.Path


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

    List<String> sanitizeMovieTitles(List<Files> files){
        List<String> titles = new ArrayList<>()
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
            titles.add(sanitizedName.trim())

        }
        return titles

    }

/*    List<Movie> getMovieInformation(String movie){

        def response
        def slurper = new JsonSlurper()
        def type = "movie"
        def url= "https://api.themoviedb.org/3/search/$type?api_key=$apiKey&language=en-US&query=$movie&page=1"
        List<Movie> movieList = new ArrayList<>()

        def httpConnection = new URL(url).openConnection()
        if(httpConnection.responseCode == httpConnection.HTTP_OK){
            response = slurper.parse(httpConnection.inputStream.newReader())
            response.results.each{
                def artPath = it.backdrop_path
                byte[] picture = getPoster(artPath,300)
                if(picture !=null) {
                    movieList.add(new Movie(title: it.original_title, image: picture, imageURL:baseImageURL+"300"+artPath))

                }
                else{
                    movieList.add(new Movie(title: it.original_title))
                }
            }
        }

        return movieList
    }*/


    List<Movie> getAllLocalMovieInformation(String path){
        List<Files> files = getFiles(path)
        List<String> titles = sanitizeMovieTitles(files)
        List<Movie> movies = new ArrayList<>()
        titles.each { movies.add(getMovieInformation(it))}
        movies = movies.findAll {it != null}
        println "$movies.size() movies found!"
        return movies
    }
    Movie getMovieInformation(String movieQuery) {

        def response
        def slurper = new JsonSlurper()
        def type = "movie"
        def urlEncodedQuery = URLEncoder.encode(movieQuery,"UTF-8")
        def url = "https://api.themoviedb.org/3/search/$type?api_key=$apiKey&language=en-US&query=$urlEncodedQuery&page=1"
        Movie movie
        def httpConnection = new URL(url).openConnection()
        if (httpConnection.responseCode == httpConnection.HTTP_OK) {
            response = slurper.parse(httpConnection.inputStream.newReader())
            def result = response.results[0]
            if(result != null) {
                def artPath = result.poster_path
                byte[] picture = getPoster(artPath, 300)
                if (picture != null) {
                    movie = new Movie(title: result.original_title, image: picture, imageURL: baseImageURL + "300" + artPath)

                } else {
                    movie = new Movie(title: result.original_title)
                }
            }
            else{
                println "No movie data found for $movieQuery"
            }
        }
        else{
            println "Http Response for $movieQuery resulted in $httpConnection.responseCode"
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
