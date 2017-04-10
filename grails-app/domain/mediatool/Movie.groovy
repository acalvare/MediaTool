package mediatool

class Movie {

    static constraints = {
        image nullable: true
        imageURL nullable: true
        length nullable: true
        genre nullable: true
    }

    byte[] image
    String imageURL
    String title
    String path
    def length
    def genre

}
