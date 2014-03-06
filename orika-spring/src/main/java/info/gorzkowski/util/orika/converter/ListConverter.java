package info.gorzkowski.util.orika.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts one list to the list with target elements, keeping the list ordering.
 * 
 * @author rgorzkowski
 * @param <S>
 *            element type of the source list
 * @param <T>
 *            element type of the target list
 */
public class ListConverter<S, T> implements Converter<List<S>, List<T>> {

    private Converter<S, T> rowConverter;

    public ListConverter() {
    }

    public ListConverter(Converter<S, T> rowConverter) {
        super();
        this.rowConverter = rowConverter;
    }

    @Override
    public List<T> convert(List<S> sourceList) {
        final List<T> results;
        if (sourceList == null) {
            results = null;
        } else {
            results = new ArrayList<T>(sourceList.size());

            for (S source : sourceList) {
                results.add(rowConverter.convert(source));
            }
        }
        return results;
    }

    public void setRowConverter(Converter<S, T> rowConverter) {
        this.rowConverter = rowConverter;
    }
}
