package pl.bloniarz.bis.service;

import org.springframework.stereotype.Component;
import pl.bloniarz.bis.model.dao.item.ItemEntity;
import pl.bloniarz.bis.model.dao.item.StatsEquationEntity;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;
import pl.bloniarz.bis.model.dto.response.ItemResponse;

import java.util.List;

@Component
public class ItemUtil {
    public ItemResponse parseItemEntityToItem(ItemEntity item, int itemLevel, int socket, String slotName) {
        ItemTypes type = item.getType();
        ItemResponse itemResponse = ItemResponse.builder()
                .slot(slotName)
                .name(item.getName())
                .itemLevel(itemLevel)
                .socket(socket)
                .dropInstance(item.getDropInstance())
                .wowheadLink(item.getWowheadLink())
                .type(type)
                .build();

        List<StatsEquationEntity> equations = item.getStats();

        StatsEquationEntity stamina = StatsEquationEntity.builder().build();
        StatsEquationEntity secondaryStat = StatsEquationEntity.builder().build();
        StatsEquationEntity mainStat = StatsEquationEntity.builder().build();
        StatsEquationEntity mainStatInt = StatsEquationEntity.builder().name("none").build();
        for (StatsEquationEntity equation: equations) {
            if(equation.getName().contains("STAMINA"))
                stamina = equation;
            if(equation.getName().contains("SECONDARY"))
                secondaryStat = equation;
            if(equation.getName().contains("MAIN") && !(equation.getName().contains("MAIN_INT")))
                mainStat = equation;
            if(equation.getName().contains("MAIN_INT"))
                mainStatInt = equation;
        }
        if(mainStatInt.getName().equals("none"))
            mainStatInt = mainStat;

        itemResponse.setStamina(
                (int)(item.getStamina() * stamina.calculate(itemLevel))
        );
        int secondary = (int)(item.getSecondary() * secondaryStat.calculate(itemLevel));
        itemResponse.setCriticalStrike(
                (int)(item.getCriticalStrike() * secondary)
        );
        itemResponse.setHaste(
                (int)(item.getHaste() * secondary)
        );
        itemResponse.setMastery(
                (int)(item.getMastery() * secondary)
        );
        itemResponse.setVersatility(
                (int)(item.getVersatility() * secondary)
        );
        itemResponse.setStrength(
                (int)(item.getStrength() * mainStat.calculate(itemLevel))
        );
        itemResponse.setAgility(
                (int)(item.getAgility() * mainStat.calculate(itemLevel))
        );
        itemResponse.setIntelligence(
                (int)(item.getIntelligence() * mainStatInt.calculate(itemLevel))
        );
        return itemResponse;
    }
}
