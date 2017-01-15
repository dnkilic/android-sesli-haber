package dnkilic.anadoluajans;

import java.util.ArrayList;

import dnkilic.anadoluajans.data.News;

public interface NewsResultListener {
    void onSuccess(ArrayList<News> news);
    void onFail();
}
