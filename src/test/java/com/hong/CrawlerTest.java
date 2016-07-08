package com.hong;

import java.util.regex.Pattern;

import org.junit.Test;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author huanggh
 * @since 2016年7月7日
 *
 */
public class CrawlerTest {
    @Test
    public void testCrawler() throws Exception {
	long beginTime = System.currentTimeMillis();
	
        String crawlStorageFolder = "data/crawl/root";
        int numberOfCrawlers = 20;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed(PropertiesUtil.get("crawler.url"));

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
        
        long endTime = System.currentTimeMillis();
        
        long difTime = endTime - beginTime;
        
        long hour = difTime / (1000*60*60);
        long min = (difTime % (1000*60*60)) / (1000*60);
        long second = ((difTime % (1000*60*60)) % (1000*60)) / 1000;
        long ss = ((difTime % (1000*60*60)) % (1000*60)) % 1000;
        
        System.out.printf("耗时：%s:%s:%s.%s", hour, min, second, ss);
    }
    
    @Test
    public void testReg() {
	System.out.println(Pattern.compile("http://deu\\.youbianku\\.com\\/category\\/postcode\\/\\w*").matcher("http://deu.youbianku.com/category/postcodse/dddd123").matches());
    }
    
    @Test
    public void testParseTime() throws InterruptedException {
	long beginTime = System.currentTimeMillis();
	
	Thread.sleep(5003);
	
	long endTime = System.currentTimeMillis();
	
	long difTime = endTime - beginTime;

	System.out.println(difTime);
	
        long hour = difTime / (1000*60*60);
        long min = (difTime % (1000*60*60)) / (1000*60);
        long second = ((difTime % (1000*60*60)) % (1000*60)) / 1000;
        long ss = ((difTime % (1000*60*60)) % (1000*60)) % 1000;
        
        System.out.printf("耗时：%s:%s:%s.%s", hour, min, second, ss);
    }
}
