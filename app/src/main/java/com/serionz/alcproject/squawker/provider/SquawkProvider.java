package com.serionz.alcproject.squawker.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by johnpaulseremba on 06/04/2018.
 */

@ContentProvider(
        authority = SquawkProvider.AUTHORITY,
        database = SquawkDatabase.class
)
public class SquawkProvider {

    public static final String AUTHORITY = "com.serionz.alcproject.squawker.provider.provider";

    @TableEndpoint(table = SquawkDatabase.SQUAWK_MESSAGES)
    public static class SquawkMessages {

        @ContentUri(
                path = "messages",
                type = "vnd.android.cursor.dir/messages",
                defaultSort = SquawkContract.COLUMN_DATE + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/messages");
    }
}
