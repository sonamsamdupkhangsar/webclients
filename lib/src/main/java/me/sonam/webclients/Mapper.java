package me.sonam.webclients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Mapper {
    private static final Logger LOG = LoggerFactory.getLogger(Mapper.class);

    public static String getJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        LOG.info("get json for object");

        try {
            return mapper.writeValueAsString(object);
        }
        catch (Exception e) {
            LOG.error("failed to convert object to json", e);
            return null;
        }
    }
}
