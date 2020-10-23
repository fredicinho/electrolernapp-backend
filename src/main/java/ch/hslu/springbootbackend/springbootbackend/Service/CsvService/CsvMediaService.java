package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Media;
import ch.hslu.springbootbackend.springbootbackend.Repository.MediaRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CsvMediaService implements CsvService {
    static String[] HEADER_MEDIA = {"mediaId", "path"};

    private final MediaRepository mediaRepository;

    public CsvMediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    // CSV Media Service
    public List<Media> saveNewEntities(MultipartFile file) {
        try {
            List<Media> medias = parseCsv(file.getInputStream());
            return mediaRepository.saveAll(medias);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Media> parseCsv(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Media> newMedias = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                int mediaId = Integer.parseInt(csvRecord.get("mediaId"));
                String mediaPath = csvRecord.get("path");
                if (checkIfMediaExists(mediaId, mediaPath)) {
                    newMedias.add(new Media(mediaId, mediaPath, this.getTypeOfMedia(mediaPath)));
                }
            }

            return newMedias;

        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }

    private boolean checkIfMediaExists(final int mediaId, final String mediaPath) {
        return mediaRepository.findByIdAndPath(mediaId, mediaPath).isEmpty();
    }

    private String getTypeOfMedia(final String mediaPath) {
        Pattern jpgPattern = Pattern.compile(".jpg", Pattern.CASE_INSENSITIVE);
        if (jpgPattern.matcher(mediaPath).find()) {
            return "JPG";
        }
        Pattern gifPattern = Pattern.compile(".gif", Pattern.CASE_INSENSITIVE);
        if (gifPattern.matcher(mediaPath).find()) {
            return "GIF";
        }
        Pattern pngPattern = Pattern.compile(".png", Pattern.CASE_INSENSITIVE);
        if (pngPattern.matcher(mediaPath).find()) {
            return "PNG";
        }
        Pattern movPattern = Pattern.compile(".mov", Pattern.CASE_INSENSITIVE);
        if (movPattern.matcher(mediaPath).find()) {
            return "MOV";
        }
        return "unknown";
    }
}
