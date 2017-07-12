package ca.joel.myapplication;

import android.support.v4.widget.SwipeRefreshLayout;

public class FeedRefresher implements SwipeRefreshLayout.OnRefreshListener {

    private FeedRefresherListener taskListener;
    private FeedRefresherListener persisterListener;
    private AfterRefreshListener afterRefresh;

    @Override
    public void onRefresh() {
        persisterListener.onRefresh();
        afterRefresh.onRefreshFinished();

        taskListener = new FeedDownloaderTask((FeedPersister)persisterListener);
        taskListener.onRefresh();
        afterRefresh.onRefreshFinished();
    }

    public void setPersisterListener(FeedRefresherListener persisterListener) {
        this.persisterListener = persisterListener;
    }

    public void setTaskListener(FeedRefresherListener taskListener) {
        this.taskListener = taskListener;
    }

    public void setAfterRefresh(AfterRefreshListener listener) {
        afterRefresh = listener;
    }
}
