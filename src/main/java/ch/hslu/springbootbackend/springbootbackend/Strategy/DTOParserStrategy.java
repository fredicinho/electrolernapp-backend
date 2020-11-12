package ch.hslu.springbootbackend.springbootbackend.Strategy;

import java.util.List;

public interface DTOParserStrategy<T> {
    T generateDTOFromObject(int id);
    T generateObjectFromDTO(T objectDTO);
    T generateDTOsFromObjects(List<T> list);
}
