package com.bistelapp.bistel.callbacks;

import com.bistelapp.bistel.database.Route;

import java.util.List;

/**
 * Created by Control & Inst. LAB on 12-Sep-16.
 */
public interface DirectionFinderListener {
     void LoadDistanceDuration(List<Route> routes);
     void LoadDistanceDuration();
}
