package com.app.githubtrending.ui.navigator;

public abstract class Router {
    public static class Details extends Router {
        private final long id;

        public Details(long id) {
            this.id = id;
        }

        public long getItemId() {
            return id;
        }
    }


    public static class PopularRepos extends Router { }
}
