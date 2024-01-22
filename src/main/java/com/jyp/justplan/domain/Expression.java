package com.jyp.justplan.domain;

import java.time.ZonedDateTime;

public class Expression {
    public static final int PLAN_TITLE_MAX_LENGTH = 50;
    public static final int TAG_NAME_MAX_LENGTH = 20;
    public static final ZonedDateTime MIN_DATE = ZonedDateTime.of(1900, 1, 1, 0, 0, 0, 0, ZonedDateTime.now().getZone());
    public static final ZonedDateTime MAX_DATE = ZonedDateTime.of(2200, 12, 31, 11, 59, 59, 999999000, ZonedDateTime.now().getZone());
}
