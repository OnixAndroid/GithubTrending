package com.app.githubtrending.di;

import com.app.githubtrending.data.repository.RepositoryImpl;
import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.repository.Repository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    Repository bindRepository() {
        return new RepositoryImpl();
    }

    @Provides
    AppSchedulers bindAppSchedulers() {
        return new AppSchedulers();
    }
}
