package pl.bloniarz.bis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.EquipmentEntity;
import pl.bloniarz.bis.model.dao.item.ItemEntity;
import pl.bloniarz.bis.model.dao.item.StatsEquationEntity;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.response.ItemResponse;
import pl.bloniarz.bis.repository.CharacterEquipmentSetRepository;
import pl.bloniarz.bis.repository.CharacterRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServicesUtil {

    private final CharacterRepository characterRepository;
    private final CharacterEquipmentSetRepository characterEquipmentSetRepository;

    public CharacterEntity getCharacterEntityFromNameAndActiveUser(String character, String activeUser) {
        return characterRepository.findByUsernameAndCharacterName(activeUser, character)
                .orElseThrow(() -> new AppException(AppErrorMessage.CHARACTER_NOT_FOUND, character));
    }

    public EquipmentEntity getSetEntityFromCharacterEntityAndSetId(long id, CharacterEntity characterEntity) {
        return characterEquipmentSetRepository.findByIdAndCharacter(id, characterEntity)
                .orElseThrow(() -> new AppException(AppErrorMessage.SET_NOT_FOUND));
    }

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
