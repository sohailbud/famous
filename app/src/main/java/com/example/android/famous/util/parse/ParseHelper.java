package com.example.android.famous.util.parse;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/30/15.
 */
public class ParseHelper {

    protected ParseObject getParseUserDetails(ParseUser parseUser) throws ParseException {
        ParseQuery<ParseObject> parseCurrentUserDetailsQuery = ParseQuery.getQuery("UserDetails");
        parseCurrentUserDetailsQuery.whereEqualTo("User", parseUser);
        return parseCurrentUserDetailsQuery.getFirst();
    }

    protected List<ParseUser> getParseUserFollowsList(ParseObject parseUserDetails) throws ParseException {
        List<ParseUser> currentUserFollowsList = new ArrayList<>();

        // build relation query and get user's follows list
        ParseRelation<ParseUser> currentUserRelationFollows = parseUserDetails.getRelation("follows");
        ParseQuery query = currentUserRelationFollows.getQuery();
        currentUserFollowsList.addAll(query.find());

        return currentUserFollowsList;
    }

    protected List<ParseUser> getParseUserFollowedByList(ParseObject parseUserDetails) throws ParseException{
        List<ParseUser> currentUserFollowedByList = new ArrayList<>();

        // build relation query and get user's follows list
        ParseRelation<ParseUser> currentUserRelationFollowedBy = parseUserDetails.getRelation("followedBy");
        ParseQuery query = currentUserRelationFollowedBy.getQuery();
        currentUserFollowedByList.addAll(query.find());

        return currentUserFollowedByList;
    }

}
