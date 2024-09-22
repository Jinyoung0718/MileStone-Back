package com.sjy.milestone.board.entity;

public enum BoardSorted {
    LATEST,
    OLDEST;

    public static BoardSorted fromValue(String value) {
        if (value.equals("최신순")) {
            return LATEST;
        } else if (value.equals("오래된순")) {
            return OLDEST;
        } else {
            throw new IllegalArgumentException("알 수 없는 값");
        }
    }
}