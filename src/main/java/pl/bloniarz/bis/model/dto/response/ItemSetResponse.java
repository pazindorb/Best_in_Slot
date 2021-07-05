package pl.bloniarz.bis.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSetResponse {

    private String characterName;
    private String name;
    private String specialization;
    private List<ItemResponse> itemList;

}
