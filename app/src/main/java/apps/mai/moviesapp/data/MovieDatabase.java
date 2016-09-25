package apps.mai.moviesapp.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Mai_ on 24-Sep-16.
 */
@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {
    public static final int VERSION = 1;


    @Table(MovieColumns.class) public static final String MOVIES = "movies";
}
