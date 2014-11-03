package com.devbyrod.distancetocity;

/**
 * Created by Rodrigo on 10/30/2014.
 */
public class Constants {

    public final static String  TAG                          = "DISTANCE TO CITY";

    public final static String  FOURSQUARE_API_URL           = "https://api.foursquare.com/v2/venues/search?";
    public final static String  FOURSQUARE_API_CLIENT_ID     = "QML4GXW4KAISGFWLVMYDOV5SPE5A4M4A10Y34PCMGUHDI45B";
    public final static String  FOURSQUARE_API_CLIENT_SECRET = "FXFOHD2W5K4YJVG4H43QHYCIVIZXNJEFP4AIB4C4BWF31TYL";
    public final static String  FOURSQUARE_API_VERSION       = "20131031";

    //Search Criteria to use
    public final static String  SEARCH_CRITERIA_NEAR         = "near";

    //return codes for HTTP request
    public final static int     HTTP_RESPONSE_CODE_OK        = 200;

    //fragments tags
    public final static String  FRAGMENT_DISPLAY_INFO_TAG   = "fragment_display_info";

    //Database constants
    public final static String  DB_NAME                     = "DistanceToCity.db";
    public final static int     DB_VERSION                  = 3;
    public final static String  DB_TABLE_RESULTS            = "TABLE_RESULTS";
    public final static String  DB_TABLE_RESULTS_COL_ID     = "_id";
    public final static String  DB_TABLE_RESULTS_COL_CITY   = "City";
    public final static String  DB_TABLE_RESULTS_COL_RESULT = "Result";

    //Shared preferences
    public final static String  SHARED_PREFERENCES_NAME     = "DistanceToCityPreferences";
    public final static String  SHARED_PREFERENCES_LAT      = "SavedLat";
    public final static String  SHARED_PREFERENCES_LON      = "SavenLon";
}
