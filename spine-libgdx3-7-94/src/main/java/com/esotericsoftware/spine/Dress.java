package com.esotericsoftware.spine;

public class Dress {

    public String slotName;

    public int slotIndex;

    public String attachmentName;

    public Dress(String slotName, int slotIndex, String attachmentName) {
        this.slotName = slotName;
        this.slotIndex = slotIndex;
        this.attachmentName = attachmentName;
    }

    @Override
    public String toString() {
        return "Dress{" +
                "slotName='" + slotName + '\'' +
                ", slotIndex=" + slotIndex +
                ", attachmentName='" + attachmentName + '\'' +
                '}';
    }
}
