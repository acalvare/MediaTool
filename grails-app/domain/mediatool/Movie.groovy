package mediatool

class Movie extends Media {

    String type = "movie"
    static hasMany = [actors: Actor]

    static constraints = {
        actors nullable: true, size: 0..1000
    }


}
