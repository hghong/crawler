package com.hong;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author huanggh
 * @since 2016年7月7日
 *
 */
public class MyCrawler extends WebCrawler {
    // private final static Pattern FILTERS =
    // Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");
    private final static String CSV_PATH = PropertiesUtil.get("csv.file.path");
    private String CSV_HEADER = PropertiesUtil.get("csv.header");
    private Pattern FILETERS = Pattern.compile(PropertiesUtil.get("crawler.url.filter"));
    private CsvWriter cw;
    private File csv;
    private Vector<String> vector = new Vector<String>();
    private String[] titleNames;

    public MyCrawler() throws IOException {
	csv = new File(CSV_PATH);

	if (csv.isFile()) {
	    csv.delete();
	}

	cw = new CsvWriter(new FileWriter(csv, true), ',');

	titleNames = CSV_HEADER.split(",");

	for (int i = 0; i < titleNames.length; i++) {
	    String titleName = StringUtils.trim(titleNames[i]);
	    titleNames[i] = titleName;

	    cw.write(titleName);
	}

	cw.write("URL");

	cw.endRecord();
	cw.close();
    }

    /**
     * This method receives two parameters. The first parameter is the page in
     * which we have discovered this new url and the second parameter is the new
     * url. You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic). In this example,
     * we are instructing the crawler to ignore urls that have css, js, git, ...
     * extensions and to only accept urls that start with
     * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
     * parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
	String href = url.getURL().toLowerCase();

	boolean bl = false;

	if (FILETERS.matcher(href).matches() && !vector.contains(href)) {
	    bl = true;
	    vector.add(href);
	}

	return bl;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
	String url = page.getWebURL().getURL();
	Map<String, String> map = new HashMap<String, String>();

	System.out.printf("正在解析:%s \n", url);

	if (page.getParseData() instanceof HtmlParseData) {
	    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	    // String text = htmlParseData.getText();
	    // Set<WebURL> links = htmlParseData.getOutgoingUrls();
	    String html = htmlParseData.getHtml();

	    Document doc = Jsoup.parse(html);
	    Elements elements = doc.select(".node.node-postcode.node-promoted.node-teaser");
	    Iterator<Element> iter = elements.iterator();

	    while (iter.hasNext()) {
		Element element = iter.next();

		for (int i = 0; i < titleNames.length; i++) {
		    String titleName = StringUtils.trim(titleNames[i]);
		    titleNames[i] = titleName;

		    map.put(titleName, "");
		}

		Elements liElements = element.select(".content ul li");
		Iterator<Element> liIterator = liElements.iterator();
		while (liIterator.hasNext()) {
		    Element liElement = liIterator.next();
		    String liTxt = liElement.text();
		    String[] txts = liTxt.split(": ");
		    if (txts.length < 2) {
			continue;
		    }

		    for (String key : map.keySet()) {
			if (key.equals(txts[0])) {
			    map.put(key, txts[1]);
			}
		    }
		}

		try {
		    cw = new CsvWriter(new FileWriter(csv, true), ',');

		    for (int i = 0; i < titleNames.length; i++) {
			String titleName = StringUtils.trim(titleNames[i]);
			titleNames[i] = titleName;

			cw.write(map.get(titleName));
		    }

		    cw.write(url);

		    cw.endRecord();
		    cw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
