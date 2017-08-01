package mediatool

import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient

import java.nio.file.Files

class MovieService {
    def messageSource

    def apiKey="a3bc5a9763a2e77b25e0ce55f9869709"
    String baseImageURL = "http://image.tmdb.org/t/p/w"
    final static String MAX_TRIES = 3

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
            titles.add(new Movie(title:sanitizedName.trim(), url:path))

        }
        return titles

    }

    List<Movie> getAllLocalMovieInformation(String path){
        List<Files> files = getFiles(path)
        List<Movie> titles = sanitizeMovieTitles(files)
        List<Movie> movies = new ArrayList<>()
        titles.each {
            Movie movie = Movie.findByUrl(it.url)
            if(movie == null){
                println "Movie with path $it.url not in database, looking up additional movie information"
                movie = getAdditonalMovieInformation(it, 0)
            } else {
                println"$movie.title found in the database!"
            }
            if(movie != null) {
                if(movie.validate()) {
                    movie.save()
                    movies << movie
                } else{
                    println movie.errors
                }
            }
        }
        println "$movies.size() movies found!"
        return movies
    }

    def loadNetflixMovies(){

    }


    Movie getAdditonalMovieInformation(Movie movie, int currentTry) {
        if(currentTry < MAX_TRIES){
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
                    movie.artURL = baseImageURL + "150" + artPath
                    movie.title = result.original_title
            }
            else{
                println "No movie data found for $movie.title"
            }
        }
         else if(httpConnection.responseCode == 429){
            println "Http Response for $movie.title resulted in $httpConnection.responseCode, sleeping for 10 seconds"
            sleep(10000)
            return getAdditonalMovieInformation(movie, currentTry++)
        }
        else{
            println "Http Response for $movie.title resulted in $httpConnection.responseCode"
        }
        return movie}
        else{
            println "Exceeded the maximum number of lookup attempts for movie $movie.title"
        }
    }

    String getNetflixContentList(){
        def url = 'https://www.allflicks.net'
        def path = '/wp-content/themes/responsive/processing/processing_us.php'
        String sessionId = getSessionCookie(url)
        def params = generateParmsMap()
        def requestHeaders = generateRequestParams(sessionId)
        List<String> titles = new ArrayList<>()
        int currentResults = 0
        int maxResults = 1000

        for(currentResults ; currentResults < maxResults ; currentResults+=100){
            params.put('start', currentResults)
            titles.add(queryServer(url, requestHeaders, path, params))
        }
    }

    Map generateParmsMap(){
        Map params = [:]
        this.class.classLoader.getResourceAsStream('params.txt').eachLine {
            def values = it.split(":")
            def key = values[0]
            def value
            if(values.size() >1) {
                value = values[1]
            } else {
                value = ""
            }
            params.put(key,value)
        }
        return params
    }
    Map generateRequestParams(String sessionId){
        Map requestHeaders = [:]
        this.class.classLoader.getResourceAsStream('headers.txt').eachLine {
            def key = it.substring(0,it.indexOf(':'))
            def value = it.substring(it.indexOf(':')+1)
            if (key == 'Cookie') {
                value = value.replace("{IDENTIFIER}", sessionId)
            }
            requestHeaders.put(key,value)
        }
        return requestHeaders
    }

    def queryServer(String url, Map requestHeaders, String path, Map params) {
        def allFlicks = new RESTClient(url)
        allFlicks.ignoreSSLIssues()
        allFlicks.headers = requestHeaders
        allFlicks.post(path: path, body: params) {
            resp, data ->
                JsonSlurper slurper = new JsonSlurper()
                def entries = slurper.parseText(data.text())
                entries.data.each {
                    println it.title
                    return it.title
                }
        }

    }

    String getSessionCookie(String url){
        String idString = "identifier="
        def allFlicks = new RESTClient(url)
        allFlicks.ignoreSSLIssues()
        allFlicks.get(path:"/"){
            resp, data ->
                def dataString = data.toString()
                String identifier = dataString.substring(dataString.indexOf(idString)+idString.length())
                return identifier.substring(0,identifier.indexOf("\""))

        }
    }



}
