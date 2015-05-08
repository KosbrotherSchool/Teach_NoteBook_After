package com.jasonko.notebook;

import android.net.Uri;
import android.provider.BaseColumns;


public class DbContract {

    public static final String DB_NAME = "notebook.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "notes";

    public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";

    // content://com.jasonko.notebook.NoteProvider
    public static final String AUTHORITY="com.jasonko.notebook.NoteProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE);

    public class Column{

        public static final String ID = BaseColumns._ID;
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String CREATED_AT = "created_at";

    }

}
