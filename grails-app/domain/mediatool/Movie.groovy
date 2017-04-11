package mediatool

class Movie extends Media {


    String type = "movie"
    static hasMany = [actors: Actor]


}
