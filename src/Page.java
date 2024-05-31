import org.jsoup.Jsoup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class Page {

    public final int MAX_ITEMS = 5;
    private String uRL;
    private String pageTitle;
    private String htmlSource;
    private String rSSURL;

    public Page(String uRL){
        setURL(uRL);
        try {
            setHtmlSource(fetchPageSource(uRL));
            setRSSURL(extractRssUrl(uRL));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setPageTitle(extractPageTitle(getHtmlSource()));
    }

    //getter and setter for private fields of the class.
    public String getHtmlSource() {
        return htmlSource;
    }
    public String getPageTitle() {
        return pageTitle;
    }
    public String getRSSURL() {
        return rSSURL;
    }
    public String getURL() {
        return uRL;
    }
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    public void setHtmlSource(String htmlSource) {
        this.htmlSource = htmlSource;
    }
    public void setRSSURL(String rSSURL) {
        this.rSSURL = rSSURL;
    }
    public void setURL(String uRL) {
        this.uRL = uRL;
    }


        //this function is for extracting html source from URL.

    private String fetchPageSource(String urlString) throws Exception
    {
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
        return toString(urlConnection.getInputStream());
    }


        //this function is for extractin RSS from URL.

    private static String extractRssUrl(String url) throws IOException
    {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        return doc.select("[type='application/rss+xml']").attr("abs:href");
    }


        //this function is for getting site title from html source.

    private String extractPageTitle(String html)
    {
        try
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(html);
            return doc.select("title").first().text();
        }
        catch (Exception e)
        {
            return "Error: no title tag found in page source!";
        }
    }


    // this function is for gathring RSS content from RSS URL.

    public void retrieveRssContent()
    {
        try {
            String rssXml = fetchPageSource(getRSSURL());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append(rssXml);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(input);
            NodeList itemNodes = doc.getElementsByTagName("item");

            for (int i = 0; i < MAX_ITEMS; ++i) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) itemNode;
                    System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent())
                    ;
                    System.out.println("Link: " + element.getElementsByTagName("link").item(0).getTextContent());
                    System.out.println("Description: " + element.getElementsByTagName("description").item(0).
                            getTextContent());
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Error in retrieving RSS content for " + getRSSURL() + ": " + e.getMessage());
        }
    }


    public String toString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null)
            stringBuilder.append(inputLine);

        return stringBuilder.toString();
    }
}
