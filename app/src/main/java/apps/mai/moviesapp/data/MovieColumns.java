package apps.mai.moviesapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by Mai_ on 24-Sep-16.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @NotNull
    @Unique
    public static final String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    public static final String DESCRIPTION = "description";

    @DataType(DataType.Type.BLOB)
    public static final String IMAGE = "image";

    @DataType(DataType.Type.TEXT)
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.REAL)
    public static final String VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.INTEGER)
    public static final String TOP_RATED = "top_rated";

    @DataType(DataType.Type.INTEGER)
    public static final String POPULAR_MOVIE = "popular_movie";

    @DataType(DataType.Type.INTEGER)
    public static final String FAVORITE = "favorite";


}
