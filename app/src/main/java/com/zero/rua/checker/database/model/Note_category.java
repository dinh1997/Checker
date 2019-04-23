package com.zero.rua.checker.database.model;

public class Note_category {
    public static final String TABLE_NAME = "note_category";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PACKAGE_APP = "pacKage";
    public static final String COLUMN_CATEGORY = "keyword";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_RANK = "rank";

    private int id;
    private String name;
    private String pacKage;
    private String category;
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
                    + COLUMN_CATEGORY + " TEXT,"
                    + COLUMN_COUNTRY + " TEXT,"
                    + COLUMN_STATUS + " INT,"
                    + COLUMN_RANK + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Note_category() {
    }

    public Note_category(int id, String name, String pacKage, String category, String country, int img_check, String rank, String timestamp) {
        this.id = id;
        this.name = name;
        this.pacKage = pacKage;
        this.category = category;
        this.timestamp = timestamp;
        this.country = country;
        this.img_check = img_check;
        this.rank = rank;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
