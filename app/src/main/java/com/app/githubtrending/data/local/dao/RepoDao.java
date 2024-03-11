package com.app.githubtrending.data.local.dao;

import static com.app.githubtrending.data.local.model.RepoEntity.ID;
import static com.app.githubtrending.data.local.model.RepoEntity.STARS_COUNT;
import static com.app.githubtrending.data.local.model.RepoEntity.TABLE_NAME;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import com.app.githubtrending.data.local.model.RepoEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RepoDao {

    @Query("SELECT * FROM " + TABLE_NAME + " ORDER BY " + STARS_COUNT + " DESC LIMIT :perPage")
    Flowable<List<RepoEntity>> getFavourites(int perPage);

    @Upsert
    Completable insertAll(RepoEntity... entity);

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " + ID + " = :id")
    Completable delete(long id);

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = :id LIMIT 1")
    Maybe<RepoEntity> getById(long id);

    @Query("SELECT EXISTS (SELECT 1 FROM " + TABLE_NAME + " WHERE id = :id LIMIT 1)")
    Single<Boolean> isExists(long id);

    @Query("SELECT COUNT(*) FROM " + TABLE_NAME)
    Single<Long> getCount();
}