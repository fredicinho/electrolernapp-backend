package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface CsvService {
    String TYPE = "text/csv";


    default boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    List saveNewEntities(MultipartFile file);

    List parseCsv(InputStream inputStream);


}
