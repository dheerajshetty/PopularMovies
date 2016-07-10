package com.example.dheerajshetty.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by dheerajshetty on 4/21/16.
 */
public class TrailerColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @References(table = MovieDatabase.MOVIES, column = MovieColumns.MOVIE_ID)
    public static final String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT)
    public static final String TRAILER_KEY = "trailer_key";
}
