package mediatool

import mediatool.Movie

/**
 * Created by alexc on 4/10/2017.
 */
class Actor {
/*    static hasMany = [media: Media]*/
    static belongsTo = [Media]
    String firstName
    String lastName
}
