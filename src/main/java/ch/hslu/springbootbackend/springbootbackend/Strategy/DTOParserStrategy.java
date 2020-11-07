package ch.hslu.springbootbackend.springbootbackend.Strategy;

import java.util.List;

public interface DTOParserStrategy<T> {
    public T generateDTOFromObject(int id);
    public T generateObjectFromDTO(T objectDTO);
    public T generateDTOsFromObjects(List<T> list);
}
