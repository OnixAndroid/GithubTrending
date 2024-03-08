package com.app.githubtrending.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.app.githubtrending.data.local.dao.RepoDao;
import com.app.githubtrending.data.local.model.RepoEntity;

@Database(entities = {RepoEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RepoDao userDao();
}