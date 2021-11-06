package pl.bloniarz.bis.model.dto.response.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OldItemCollectionResponse {

    private String dropInstance;
    private List<OldItemResponse> oldItems;
}
