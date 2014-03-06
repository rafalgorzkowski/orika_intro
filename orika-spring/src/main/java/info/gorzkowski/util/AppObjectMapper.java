package info.gorzkowski.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppObjectMapper extends ObjectMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppObjectMapper.class);

    public AppObjectMapper() {
        super();
        SimpleModule module = new SimpleModule();

        configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        if (LOGGER.isDebugEnabled()) {
            configure(SerializationFeature.INDENT_OUTPUT, true);
        }
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        registerModule(module);
    }

}