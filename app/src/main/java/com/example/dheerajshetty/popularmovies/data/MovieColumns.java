package com.example.dheerajshetty.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by dheerajshetty on 3/19/16.
 */
public class MovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "title";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POSTER = "poster";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String USER_RATING = "user_rating";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String PLOT = "plot";

    @DataType(DataType.Type.INTEGER) @NotNull
    @Unique
    public static final String MOVIE_ID = "movie_id";

}
