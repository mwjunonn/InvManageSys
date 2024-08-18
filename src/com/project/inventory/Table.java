package com.project.inventory;

public enum Table {
    ITEM("item", new String[]{"item_id", "item_name", "item_type"}),
    DANGERCLAUSE(new String[]{"DROP", "DELETE", "GRANT", "TRUNCATE"});

    private final String tableName;
    private final String[] columnName;
    private final String[] keyword;

    Table(String tableName, String[] columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.keyword = new String[]{"null"};
    }

    Table(String[] keyword){
        this.keyword = keyword;
        this.tableName = null;
        this.columnName = new String[]{"null"};
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getColumnName() {
        return columnName;
    }

    public String[] getKeyword() {
        return keyword;
    }

    public String getSpecifiedColumn(String keyword){
        for(String column : columnName){
            int index = column.toLowerCase().indexOf(keyword.toLowerCase());
            if(index != -1){
                return column;
            }
        }
        return null;
    }
}
