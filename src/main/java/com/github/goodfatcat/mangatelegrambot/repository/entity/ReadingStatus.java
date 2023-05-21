package com.github.goodfatcat.mangatelegrambot.repository.entity;

public enum ReadingStatus {
    READ(1),
    PLANED(2),
    DROPPED(3),
    HAS_READ(4),
    LOVED(5);
    private final int status;

    ReadingStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static ReadingStatus findByStatusCode(int code) {
        for (ReadingStatus value : values()) {
            if (value.getStatus() == code)
                return value;
        }
        throw new IllegalArgumentException("No such reading status");
    }
}
