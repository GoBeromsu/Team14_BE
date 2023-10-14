package com.kakaotech.team14backend.inner.image.usecase;

import com.kakaotech.team14backend.common.FileUtils;
import com.kakaotech.team14backend.inner.image.model.Image;
import com.kakaotech.team14backend.inner.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
public class CreateCrawlImageUsecase {

  private final FileUtils fileUtils;
  private final ImageRepository imageRepository;

  public Image execute(String imageUrl) throws IOException {
    // Download the image from the photo booth
    Document doc = Jsoup.connect(imageUrl).get();
    Element element = doc.select("body > div:nth-child(1) > a:nth-child(11)").first();
    String href = element.attr("href");

    // Ensure the link is a complete URL
    if (!href.startsWith("http")) {
      href = new URL(new URL(imageUrl), href).toString();
    }

    // Open a connection to the image URL
    java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new URL(
        href).openConnection();
    connection.setRequestMethod("GET");
    InputStream in = connection.getInputStream();

    // Define the path for the output file
    String outputPath = "downloaded_image.jpg";
    Path path = Path.of(outputPath);

    // Download the image
    Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);

    // Close the input stream
    in.close();

    // Convert the downloaded image file to a MultipartFile
    File file = path.toFile();
    FileInputStream fileInputStream = new FileInputStream(file);
    MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
        Files.probeContentType(path), fileInputStream);

    // Store the MultipartFile and create an Image entity
    fileUtils.storeFile(multipartFile);
    String storedFilePath = fileUtils.getFullPath(file.getName());
    Image createdImage = Image.createImage(storedFilePath);

    // Save the Image entity to the database
    return imageRepository.save(createdImage);
  }
}
