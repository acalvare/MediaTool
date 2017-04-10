package mediatool

import mediatool.Quality

/**
 * Created by alexc on 4/10/2017.
 */
abstract class Media {
    static constraints = {
        artURL nullable: true
        length nullable: true

    }

    String artURL
    String title
    String url
    int length
    boolean watched
    static hasMany = [actors: Actor]
    Date releaseDate
    Quality quality
    String source
}
