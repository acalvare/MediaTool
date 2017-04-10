package mediatool

import mediatool.Season
import mediatool.Show

/**
 * Created by alexc on 4/10/2017.
 */
class Episode extends Media{
    Show show
    Season season
    static belongsTo = [Season]
}
