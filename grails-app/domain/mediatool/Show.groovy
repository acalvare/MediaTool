package mediatool

/**
 * Created by alexc on 4/10/2017.
 */
class Show {
    String title
    static hasMany = [season: Season]
    Date startDate
    Date endDate
    int numEpisodes
    String artURL
}
