package com.cursojunit.orderapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestUtils {

    private final static Logger logger = LogManager.getLogger(TestUtils.class);

    public final static String WORK_PATH =  System.getProperty("user.dir") + File.separator
            + "src" + File.separator + "test" + File.separator
            + "resources" + File.separator + "mocks" + File.separator;

    public static <T> T convertJsonToPOJO(String fileLocation, Class<?> target) {

        if (fileLocation == null) {
            return null;
        }

        T field = null;

        ObjectMapper mapper = getDefaultMapper();

        try {

            File file = new File(fileLocation);
            field = mapper.readValue(file, mapper.getTypeFactory().constructType(Class.forName(target.getName())));

        }catch (UnrecognizedPropertyException e) {
            logger.info("INFO - Problem converting to json - front sending useless attribute - " + e.getMessage());
        }catch(Exception e) {
            logger.info("ERROR - Generic error in json handler " + e.getMessage());
            e.printStackTrace();
        }

        return field;
    }

    public static List convertJsonToList(String fileLocation, Class<?> target) {
        if (fileLocation == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {

            File file = new File(fileLocation);
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, target));

        }catch (UnrecognizedPropertyException e) {
            logger.info("INFO - Problem converting to json - front sending useless attribute - " + e.getMessage());
        }catch(Exception e) {
            logger.info("ERROR - Generic error in json handler " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String readFile(String filePath) {
        String content = "";
        String line;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            while((line = bufferedReader.readLine()) != null) {
                content += line;
                content += "\n";
            }
        }catch (Exception ex){
            content = null;
        }

        return content;
    }

    private static ObjectMapper getDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        return mapper;
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
