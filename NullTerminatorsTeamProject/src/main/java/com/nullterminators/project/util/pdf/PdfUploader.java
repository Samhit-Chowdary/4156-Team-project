package com.nullterminators.project.util.pdf;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Class for uploading pdf file to google cloud storage.
 */
@Component("PdfUploader")
public class PdfUploader {

  /**
   * Upload pdf file to google cloud storage.
   *
   * @param filePath file path
   * @return URL of the uploaded file
   */
  @SneakyThrows
  public String uploadPdf(String filePath) {

    String gcpConfigFile = "gcp-credentials.json";
    InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();
    String projectId = "hpml-413322";
    Storage storage = StorageOptions.newBuilder().setProjectId(projectId)
            .setCredentials(GoogleCredentials.fromStream(inputStream)).build().getService();
    String bucketName = "adv-sft-storage";
    BlobId blobId = BlobId.of(bucketName, filePath);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

    Storage.BlobWriteOption precondition;
    if (storage.get(bucketName, filePath) == null) {
      precondition = Storage.BlobWriteOption.doesNotExist();
    } else {
      precondition = Storage.BlobWriteOption.generationMatch(
              storage.get(bucketName, filePath).getGeneration());
    }
    storage.createFrom(blobInfo, Paths.get(filePath), precondition);

    File file = new File(filePath);
    if (!file.delete()) {
      System.out.println("Failed to delete the file.");
    }

    return "https://storage.googleapis.com/" + bucketName + "/" + filePath;
  }
}
