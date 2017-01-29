package com.dnkilic.seslihaber;

import java.util.ArrayList;

import com.dnkilic.seslihaber.data.News;

public interface NewsResultListener {
    void onSuccess(ArrayList<News> news);
    void onFail(boolean error, String errorMessage);
}
