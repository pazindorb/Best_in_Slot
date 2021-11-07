package pl.bloniarz.bis.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bloniarz.bis.model.dao.character.CharacterClasses;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.EquipmentEntity;
import pl.bloniarz.bis.model.dao.equipmentset.ItemSetEntity;
import pl.bloniarz.bis.model.dao.item.ItemEntity;
import pl.bloniarz.bis.model.dao.item.StatsEquationEntity;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.request.character.CharacterRequest;

import java.util.Collections;
import java.util.List;

class STestUtil {

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //////////////////////
    // CHARACTER ENTITY //
    //////////////////////
    static CharacterEntity getCustomizedCharacterEntity(long id, String name, CharacterClasses characterClass, UserEntity userEntity, List<EquipmentEntity> characterEquipmentSets){
        CharacterEntity characterEntity = getCustomizedCharacterEntity(id,name,characterClass);
        characterEntity.setCharacterEquipmentSets(characterEquipmentSets);
        characterEntity.setUser(userEntity);
        return null;
    }
    static CharacterEntity getCustomizedCharacterEntity(long id, String name, CharacterClasses characterClass, List<EquipmentEntity> characterEquipmentSets){
        CharacterEntity characterEntity = getCustomizedCharacterEntity(id,name,characterClass);
        characterEntity.setCharacterEquipmentSets(characterEquipmentSets);
        return null;
    }
    static CharacterEntity getCustomizedCharacterEntity(long id, String name, CharacterClasses characterClass, UserEntity userEntity){
        CharacterEntity characterEntity = getCustomizedCharacterEntity(id,name,characterClass);
        characterEntity.setUser(userEntity);
        return null;
    }
    static CharacterEntity getCustomizedCharacterEntity(long id, String name, CharacterClasses characterClass){
        return CharacterEntity.builder()
                .id(id)
                .name(name)
                .characterClass(characterClass)
                .active(true)
                .build();
    }
    static CharacterEntity getDefaultCharacterEntity(int number){
        switch (number){
            case 1:
                return CharacterEntity.builder()
                        .id(9991)
                        .characterClass(CharacterClasses.WARRIOR)
                        .active(true)
                        .name("Twar")
                        .build();
            case 2:
                return CharacterEntity.builder()
                        .id(9992)
                        .characterClass(CharacterClasses.DEATH_KNIGHT)
                        .active(true)
                        .name("Tdk")
                        .build();
            case 3:
                return CharacterEntity.builder()
                        .id(9993)
                        .characterClass(CharacterClasses.MONK)
                        .active(true)
                        .name("Tmonk")
                        .build();
            case 4:
                return CharacterEntity.builder()
                        .id(9994)
                        .characterClass(CharacterClasses.PALADIN)
                        .active(true)
                        .name("Tpal")
                        .build();
            default:
                return getDefaultCharacterEntity();
        }




    }
    static CharacterEntity getDefaultCharacterEntity(){
        return CharacterEntity.builder()
                .id(9999)
                .characterClass(CharacterClasses.MAGE)
                .active(true)
                .name("Tmage")
                .build();
    }

    ///////////////////////
    // CHARACTER REQUEST //
    ///////////////////////
    static CharacterRequest getDefaultCharacterRequest() {
        return CharacterRequest.builder()
                .name("Tmage")
                .characterClass("MAGE")
                .build();
    }

    /////////////////
    // USER ENTITY //
    /////////////////
    static UserEntity getDefaultUserEntity(int number){
        switch (number){
            default:
                return getDefaultUserEntity();
        }
    }
    static UserEntity getDefaultUserEntity(){
        return UserEntity.builder()
                .id(9999)
                .login("TestUser")
                .email("test@test.test")
                .password(passwordEncoder.encode("password"))
                .active(true)
                .build();
    }

    //////////////////////
    // EQUIPMENT ENTITY //
    //////////////////////
    static EquipmentEntity getDefaultEquipmentEntity(int number){
        switch (number){
            default:
                return getDefaultEquipmentEntity();
        }

    }
    static EquipmentEntity getDefaultEquipmentEntity(){
        ItemSetEntity itemSetEntity = getDefaultItemSetEntity();
        return EquipmentEntity.builder()
                .id(9999)
                .name("TestEquipmentEntity")
                .specialization("TestSpec")
                .active(true)
                .head(itemSetEntity)
                .neck(itemSetEntity)
                .shoulders(itemSetEntity)
                .chest(itemSetEntity)
                .back(itemSetEntity)
                .wrists(itemSetEntity)
                .hands(itemSetEntity)
                .waist(itemSetEntity)
                .legs(itemSetEntity)
                .feet(itemSetEntity)
                .firstRing(itemSetEntity)
                .secondRing(itemSetEntity)
                .firstTrinket(itemSetEntity)
                .secondTrinket(itemSetEntity)
                .mainHand(itemSetEntity)
                .offHand(itemSetEntity)
                .build();
    }

    /////////////////////
    // ITEM SET ENTITY //
    /////////////////////
    static ItemSetEntity getDefaultItemSetEntity(int number){
        switch (number){
            default:
                return getDefaultItemSetEntity();
        }
    }
    static ItemSetEntity getDefaultItemSetEntity(){
        return ItemSetEntity.builder()
                .id(9999)
                .item(getDefaultItemEntity())
                .itemLevel(100)
                .socket(1)
                .build();
    }

    /////////////////
    // ITEM ENTITY //
    /////////////////
    static ItemEntity getDefaultItemEntity(int number){
        switch (number){
            default:
                return getDefaultItemEntity();
        }
    }
    static ItemEntity getDefaultItemEntity(){
        StatsEquationEntity statsEquationEntity = getDefaultStatsEquationEntity();
        return ItemEntity.builder()
                .id(9999)
                .name("TestItemName")
                .type(ItemTypes.BOW)
                .slot(ItemSlots.TH_WEAPON)
                .stats(Collections.singletonList(statsEquationEntity))
                .stamina(1)
                .strength(1)
                .secondary(1)
                .agility(1)
                .intelligence(1)
                .mastery(1)
                .criticalStrike(1)
                .haste(1)
                .versatility(1)
                .old(false)
                .dropInstance("TestInstance")
                .wowheadLink("TestLink")
                .build();
    }

    //////////////////////////
    // ITEM EQUATION ENTITY //
    //////////////////////////
    static StatsEquationEntity getDefaultStatsEquationEntity(int number){
        switch (number){
            default:
                return getDefaultStatsEquationEntity();
        }
    }
    static StatsEquationEntity getDefaultStatsEquationEntity(){
        return StatsEquationEntity.builder()
                .id(9999)
                .name("TestStatsEquation")
                .x0(0.5)
                .x1(0.4)
                .x2(0.3)
                .x3(0.2)
                .build();
    }

}
