package org.niewie.personapi.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Demo personID generator (ban cause collisions)
 *
 * @author aniewielska
 * @since 19/07/2018
 */
@Component
public class IdGenerator {

    private static final int LENGTH = 4;
    private static final String PREFIX = "PRS-";

    public String getNext() {
        byte[] buffer = new byte[LENGTH];
        ThreadLocalRandom.current().nextBytes(buffer);
        String rand = IntStream.range(0, buffer.length).mapToObj(i -> String.format("%02x", buffer[i])).collect(Collectors.joining());
        return PREFIX + rand;
    }


}
