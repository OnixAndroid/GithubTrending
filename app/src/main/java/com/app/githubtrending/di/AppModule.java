package com.app.githubtrending.di;

import android.content.Context;

import androidx.room.Room;

import com.app.githubtrending.data.client.GithubApiClient;
import com.app.githubtrending.data.local.AppDatabase;
import com.app.githubtrending.data.local.dao.RepoDao;
import com.app.githubtrending.data.remote.GithubApiService;
import com.app.githubtrending.data.repository.RepositoryImpl;
import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    Repository bindRepository(GithubApiService githubApiService, RepoDao repoDao) {
        return new RepositoryImpl(githubApiService, repoDao);
    }

    @Provides
    AppSchedulers bindAppSchedulers() {
        return new AppSchedulers();
    }

    @Provides
    @Singleton
    AppDatabase provideAppSchedulers(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "app_database").build();
    }

    @Provides
    RepoDao provideRepoDao(AppDatabase appDatabase) {
        return appDatabase.userDao();
    }

    @Provides
    GithubApiClient provideGithubApiClient() {
        return new GithubApiClient();
    }

    @Provides
    GithubApiService provideGithubApiService(GithubApiClient githubApiClient) {
        return githubApiClient.getGithubApiService();
    }
}
