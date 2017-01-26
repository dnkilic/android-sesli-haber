package dnkilic.seslihaber;

import java.util.ArrayList;

import dnkilic.seslihaber.data.News;

public interface NewsResultListener {
    void onSuccess(ArrayList<News> news);
    void onFail(boolean error, String errorMessage);
}
