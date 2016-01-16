package com.caixu.beanvalidateutil.imlclass;

/**
 * Created by xu on 2015/12/4.
 */
public class VVResult {
    private Integer columnIndex;//列
    private Integer rowIndex;
    private String message;

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
