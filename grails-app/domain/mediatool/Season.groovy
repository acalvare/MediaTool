package mediatool

/**
 * Created by alexc on 4/10/2017.
 */
class Season {
    int seasonNumber
    static hasMany = [episodes: Episode]
    static belongsTo = [Show]
    Date releaseDate
    Date endDate
    int numEpisodes
}
