package com.app.githubtrending.domain.model;

import com.app.githubtrending.R;

public enum Filter {
    LastMonth(R.string.last_month),
    LastWeek(R.string.last_week),
    LastDay(R.string.last_day);

    Filter(int filterNameRes) {
        this.filterNameRes = filterNameRes;
    }

    public final int filterNameRes;
}
