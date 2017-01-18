package dnkilic.anadoluajans;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import dnkilic.anadoluajans.data.News;

public class RssFeedParser extends AsyncTask<String, Void, ArrayList<News>> {

    private NewsResultListener listener;

    public RssFeedParser(NewsResultListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<News> doInBackground(String... params) {

        ArrayList<News> newsList = new ArrayList<>();

        String selectedCategory = params[0];
        String url = "http://aa.com.tr/tr/rss/default?cat=" + selectedCategory;

        try {
            URL xmlUrl = new URL(url);
            InputStream stream = xmlUrl.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(stream);

            NodeList list = doc.getElementsByTagName("item");

            for(int i = 0 ; i < list.getLength() ; i++)
            {
                Element element = (Element) list.item(i);
                String link = element.getElementsByTagName("link").item(0).getFirstChild().getTextContent();
                String description = element.getElementsByTagName("description").item(0).getFirstChild().getTextContent();
                String title = element.getElementsByTagName("title").item(0).getFirstChild().getTextContent();
                String image = element.getElementsByTagName("image").item(0).getFirstChild().getTextContent();
                String pubDate = element.getElementsByTagName("pubDate").item(0).getFirstChild().getTextContent();

                String normalizedDate = DateParser.parse(pubDate);

                News news = new News(title, description, image, link, normalizedDate);
                newsList.add(news);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return newsList;
    }

    @Override
    protected void onPostExecute(ArrayList<News> news) {
        super.onPostExecute(news);

        if(news != null && !news.isEmpty())
        {
            listener.onSuccess(news);
        }
        else
        {
            listener.onFail();
        }
    }

}
