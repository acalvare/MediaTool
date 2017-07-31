package mediatool

import mediatool.Quality

/**
 * Created by alexc on 4/10/2017.
 */
abstract class Media {

    String artURL
    String title
    String url
    int length
    boolean watched
    Date releaseDate
    Quality quality
    String source

    static constraints = {
        artURL (size: 0..1000, nullable: true)
        url (size: 0..10000)
        length nullable: true
        watched nullable: true
        releaseDate nullable: true
        quality nullable: true
        source nullable: true

    }
}
