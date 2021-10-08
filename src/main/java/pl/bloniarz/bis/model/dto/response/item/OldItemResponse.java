package pl.bloniarz.bis.model.dto.response.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OldItemResponse {

    private long id;
    private String name;
    private String wowheadLink;

}
