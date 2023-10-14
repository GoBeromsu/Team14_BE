package com.kakaotech.team14backend.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PhotoBoothCrawler {

  private String url;

  public PhotoBoothCrawler(String url) {
    this.url = url;
  }

  public void downloadImage() {
    try {
      Document doc = Jsoup.connect(url).get();
      Element element = doc.select("body > div:nth-child(1) > a:nth-child(11)").first();
      String href = element.attr("href");

      // Ensure the link is a complete URL
      if (!href.startsWith("http")) {
        href = new URL(new URL(url), href).toString();
      }

      // Open a connection to the image URL
      HttpURLConnection connection = (HttpURLConnection) new URL(href).openConnection();
      connection.setRequestMethod("GET");
      InputStream in = connection.getInputStream();

      // Define the path for the output file
      String outputPath = "downloaded_image.jpg";

      // Download the image
      Files.copy(in, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);

      // Close the input stream
      in.close();


    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) {
    PhotoBoothCrawler crawler = new PhotoBoothCrawler(
        "http://photoqr.kr/R/jn05/231012/215425/index.html");
    crawler.downloadImage();
  }
}
