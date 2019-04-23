package com.zero.rua.checker.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Note_keyword {
    public static final String TABLE_NAME = "notes_keyword";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PACKAGE_APP = "pacKage";
    public static final String COLUMN_KEYWORD = "keyword";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_RANK = "rank";

    private int id;
    private String name;
    private String pacKage;
    private String keyword;
    private String timestamp;
    private String country;
    private int img_check;
    private String rank;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PACKAGE_APP + " TEXT,"
                    + COLUMN_KEYWORD + " TEXT,"
                    + COLUMN_COUNTRY + " TEXT,"
                    + COLUMN_STATUS + " INT,"
                    + COLUMN_RANK + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

        public Note_keyword() {
        }

    public Note_keyword(int id, String pacKage, String keyword, String country, String timestamp) {
        this.id = id;
        this.pacKage = pacKage;
        this.keyword = keyword;
        this.timestamp = timestamp;
        this.country = country;
    }

    public Note_keyword(int id, String name, String pacKage, String keyword, String country, int img_check, String rank, String timestamp) {
        this.id = id;
        this.name = name;
        this.pacKage = pacKage;
        this.keyword = keyword;
        this.timestamp = timestamp;
        this.country = country;
        this.rank = rank;
        this.img_check = img_check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPacKage() {
        return pacKage;
    }

    public void setPacKage(String pacKage) {
        this.pacKage = pacKage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getImg_check() {
        return img_check;
    }

    public void setImg_check(int img_check) {
        this.img_check = img_check;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
