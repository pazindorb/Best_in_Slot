package pl.bloniarz.bis.externalapi.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WowheadItemResponse {
    @JacksonXmlProperty(localName = "id")
    private int id;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "quality")
    private String quality;

    @JacksonXmlProperty(localName = "level")
    private int itemLevel;

    @JacksonXmlProperty(localName = "class")
    private String category;

    @JacksonXmlProperty(localName = "subclass")
    private String type;

    @JacksonXmlProperty(localName = "inventorySlot")
    private String slot;

    @JacksonXmlProperty(localName = "jsonEquip")
    private String statJsonData;

    @JacksonXmlProperty(localName = "link")
    private String wowHeadLink;

    private String dropInstance;

    private WowheadItemResponseStat stats;

    @Override
    public String toString() {
        return "WowheadItemResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemLevel=" + itemLevel +
                ", category=" + category +
                ", type=" + type +
                ", slot=" + slot +
                ",\n statJsonData='" + statJsonData + '\'' +
                ",\n stats=" + stats +
                "}\n\n";
    }
}
