package com.mt.generator;

import com.mt.model.Msg;
import org.apache.commons.lang3.RandomStringUtils;

public class MsgGenerator {

    public static Msg.Wrapper generate() {
        return Msg.Wrapper.newBuilder()
            .setTag(String.format("Tag-%s", RandomStringUtils.randomNumeric(1)))
            .addPackets(buildPacket(genId(), genName()))
            .addPackets(buildPacket(genId(), genName()))
            .build();
    }

    private static Msg.Packet buildPacket(int id, String name) {
        return Msg.Packet.newBuilder()
            .setId(id)
            .setName(name)
            .build();
    }

    private static int genId() {
        return Integer.parseInt(RandomStringUtils.randomNumeric(3));
    }

    private static String genName() {
        int len = Integer.parseInt(RandomStringUtils.randomNumeric(1));
        return RandomStringUtils.randomAlphabetic(len);
    }
}
