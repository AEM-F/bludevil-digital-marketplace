package nl.fhict.digitalmarketplace.service.enums;

import nl.fhict.digitalmarketplace.model.enums.RetrievalMode;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class StringToRetrievalEnumConverter implements Converter<String, RetrievalMode> {
    @Override
    public RetrievalMode convert(String source) {
        try {
            return RetrievalMode.valueOf(source.toUpperCase(Locale.ROOT));
        }catch (IllegalArgumentException e){
            return null;
        }
    }
}
