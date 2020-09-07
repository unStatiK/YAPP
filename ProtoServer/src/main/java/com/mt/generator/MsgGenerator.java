package com.mt.generator;

import com.mt.model.Msg;

public class MsgGenerator {
    //todo: remove hardcode
    public static Msg.Wrapper generate() {
        return Msg.Wrapper.newBuilder()
            .setTag("zer0")
            .addPackets(buildPacket(99, "zero"))
            .addPackets(buildPacket(678, "next"))
            .build();
    }

    private static Msg.Packet buildPacket(int id, String name) {
        return Msg.Packet.newBuilder()
            .setId(id)
            .setName(name)
            .build();
    }
}
