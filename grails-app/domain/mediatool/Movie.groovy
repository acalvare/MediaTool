package mediatool

class Movie {

    static constraints = {
        id name:path
    }

    byte[] image
    String imageURL
    String title
    String path
    def length
    def genre

}
