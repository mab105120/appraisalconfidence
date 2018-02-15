package edu.grenoble.em.bourji;

import com.auth0.jwk.JwkException;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Moe on 8/22/17.
 */
public class Playground {


    @Test
    public void test() throws IOException, JwkException {
        System.out.println(Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
//        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

    }
}