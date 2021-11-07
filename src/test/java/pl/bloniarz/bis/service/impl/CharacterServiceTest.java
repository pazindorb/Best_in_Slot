package pl.bloniarz.bis.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.model.dto.exceptions.AppErrorMessage;
import pl.bloniarz.bis.model.dto.exceptions.AppException;
import pl.bloniarz.bis.model.dto.request.character.CharacterRequest;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponse;
import pl.bloniarz.bis.model.dto.response.character.CharacterResponseWithCollections;
import pl.bloniarz.bis.model.dto.response.character.UsersCharactersResponse;
import pl.bloniarz.bis.repository.CharacterRepository;
import pl.bloniarz.bis.repository.EquipmentSetRepository;
import pl.bloniarz.bis.repository.UserRepository;
import pl.bloniarz.bis.service.ServicesUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EquipmentSetRepository equipmentSetRepository;

    @Test
    public void getAllCharactersForUser_username_shouldReturnUserCharactersResponseWithListOfCharacters(){
        //Before
        String testUsername = "TestUser";
        List<CharacterEntity> characterEntities = List.of(
                STestUtil.getDefaultCharacterEntity(1),
                STestUtil.getDefaultCharacterEntity(2)
        );
        int numberOfSets = 2;
        when(characterRepository.findAllCharactersOfUserNamed(any(String.class)))
                .thenReturn(characterEntities);
        when(equipmentSetRepository.getCountOfSetForCharacterId(any(Long.class)))
                .thenReturn(numberOfSets);

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, userRepository, equipmentSetRepository);

        //When
        UsersCharactersResponse response = characterService.getAllCharactersForUser(testUsername);

        //Then
        List<CharacterResponse> characterResponses = response.getCharacters();

        verify(characterRepository, times(1)).findAllCharactersOfUserNamed(any(String.class));
        verify(equipmentSetRepository, times(2)).getCountOfSetForCharacterId(any(Long.class));
        assertEquals(characterEntities.size(), characterResponses.size());
        for(int i = 0; i < characterResponses.size(); i++){
            assertEquals(characterEntities.get(i).getName(), characterResponses.get(i).getName());
            assertEquals(characterEntities.get(i).getId(), characterResponses.get(i).getId());
            assertEquals(characterEntities.get(i).getCharacterClass().toString(), characterResponses.get(i).getCharacterClass());
            assertEquals(numberOfSets, characterResponses.get(i).getNumberOfSets());
        }
    }
    @Test
    public void getAllCharactersForUser_id_shouldReturnUserCharactersResponseWithListOfCharacters(){
        //Before
        long testId = 9999;
        List<CharacterEntity> characterEntities = List.of(
                STestUtil.getDefaultCharacterEntity(1),
                STestUtil.getDefaultCharacterEntity(2)
        );
        int numberOfSets = 2;
        when(characterRepository.findAllCharactersOfUserId(any(Long.class)))
                .thenReturn(characterEntities);
        when(equipmentSetRepository.getCountOfSetForCharacterId(any(Long.class)))
                .thenReturn(numberOfSets);

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, userRepository, equipmentSetRepository);

        //When
        UsersCharactersResponse response = characterService.getAllCharactersForUser(testId);

        //Then
        List<CharacterResponse> characterResponses = response.getCharacters();

        verify(characterRepository, times(1)).findAllCharactersOfUserId(any(Long.class));
        verify(equipmentSetRepository, times(2)).getCountOfSetForCharacterId(any(Long.class));
        assertEquals(characterEntities.size(), characterResponses.size());
        for(int i = 0; i < characterResponses.size(); i++){
            assertEquals(characterEntities.get(i).getName(), characterResponses.get(i).getName());
            assertEquals(characterEntities.get(i).getId(), characterResponses.get(i).getId());
            assertEquals(characterEntities.get(i).getCharacterClass().toString(), characterResponses.get(i).getCharacterClass());
            assertEquals(numberOfSets, characterResponses.get(i).getNumberOfSets());
        }
    }

    @Test
    public void addCharacter_shouldReturnCreatedCharacter(){
        //Before
        String username = "TestUser";
        CharacterRequest characterRequest = STestUtil.getDefaultCharacterRequest();
        UserEntity userEntity = STestUtil.getDefaultUserEntity();
        CharacterEntity characterEntity = STestUtil.getDefaultCharacterEntity();
        int numberOfSets = 2;
        when(userRepository.findByLoginAndActiveIsTrue(any(String.class)))
                .thenReturn(Optional.ofNullable(userEntity));
        when(characterRepository.save(any(CharacterEntity.class)))
                .thenReturn(characterEntity);
        when(equipmentSetRepository.getCountOfSetForCharacterId(any(Long.class)))
                .thenReturn(numberOfSets);

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, userRepository, equipmentSetRepository);

        //When
        CharacterResponse characterResponse = characterService.addCharacter(characterRequest, username);

        //Then
        verify(userRepository, times(1)).findByLoginAndActiveIsTrue(any(String.class));
        verify(characterRepository, times(1)).save(any(CharacterEntity.class));
        verify(equipmentSetRepository, times(1)).getCountOfSetForCharacterId(any(Long.class));

        ArgumentCaptor<CharacterEntity> captor = ArgumentCaptor.forClass(CharacterEntity.class);
        verify(characterRepository).save(captor.capture());

        assertEquals(userEntity, captor.getValue().getUser());
        assertEquals(characterEntity.getId(), characterResponse.getId());
        assertEquals(characterEntity.getName(), characterResponse.getName());
        assertEquals(characterEntity.getCharacterClass().toString(), characterResponse.getCharacterClass());
        assertEquals(numberOfSets, characterResponse.getNumberOfSets());
    }
    @Test
    public void addCharacter_shouldThrowClassDoNotExist(){
        //Before
        String username = "TestUser";
        CharacterRequest characterRequest = STestUtil.getDefaultCharacterRequest();
        characterRequest.setCharacterClass("NotAClass");
        UserEntity userEntity = STestUtil.getDefaultUserEntity();
        when(userRepository.findByLoginAndActiveIsTrue(any(String.class)))
                .thenReturn(Optional.ofNullable(userEntity));

        //Object
        CharacterService characterService = new CharacterService(null, null, userRepository, null);

        //When
        AppException exception = assertThrows(AppException.class, () ->
                characterService.addCharacter(characterRequest, username)
        );

        //Then
        assertEquals(AppErrorMessage.CLASS_DO_NOT_EXIST.getMessage(), exception.getMessage());
        assertEquals(AppErrorMessage.CLASS_DO_NOT_EXIST.getStatus(), exception.getResponseStatus());
    }
    @Test
    public void addCharacter_shouldThrowUserNotFoundException(){
        //Before
        String username = "TestUser";
        CharacterRequest characterRequest = STestUtil.getDefaultCharacterRequest();
        when(userRepository.findByLoginAndActiveIsTrue(any(String.class)))
                .thenReturn(Optional.empty());

        //Object
        CharacterService characterService = new CharacterService(null,null,userRepository,null);

        //When/Then
        AppException exception = assertThrows(AppException.class, () ->
            characterService.addCharacter(characterRequest, username)
        );
        assertEquals(AppErrorMessage.USER_NOT_FOUND.getStatus(), exception.getResponseStatus());
        assertEquals("TestUser not found.", exception.getMessage());
    }

    @Test
    public void deleteCharacter_shouldSoftDeleteSuccessfully(){
        //Before
        String username = "TestUser";
        long id = 9999;
        UserEntity userEntity = STestUtil.getDefaultUserEntity();
        CharacterEntity characterEntity = STestUtil.getDefaultCharacterEntity();
        characterEntity.setUser(userEntity);
        characterEntity.setCharacterEquipmentSets(new ArrayList<>());
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.of(characterEntity));

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, null, null);

        //When
        characterService.deleteCharacter(id, username);

        ArgumentCaptor<CharacterEntity> captor = ArgumentCaptor.forClass(CharacterEntity.class);
        verify(characterRepository).save(captor.capture());
        assertEquals("", captor.getValue().getName());
        assertFalse(captor.getValue().isActive());
    }
    @Test
    public void deleteCharacter_shouldThrowNotOwnerExceptionWhenCharacterNotFound(){
        //Before
        String username = "TestUser";
        long id = 9999;
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.empty());

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, null, null);

        //When
        AppException exception = assertThrows(AppException.class, () ->
            characterService.deleteCharacter(id, username)
        );

        assertEquals(AppErrorMessage.NOT_OWNER.getMessage(), exception.getMessage());
        assertEquals(AppErrorMessage.NOT_OWNER.getStatus(), exception.getResponseStatus());
    }
    @Test
    public void deleteCharacter_shouldThrowNotOwnerExceptionWhenUsernamesDoesntMach(){
        //Before
        String username = "NotSameUser";
        long id = 9999;
        UserEntity userEntity = STestUtil.getDefaultUserEntity();
        CharacterEntity characterEntity = STestUtil.getDefaultCharacterEntity();
        characterEntity.setUser(userEntity);
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.of(characterEntity));

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, null, null);

        //When
        AppException exception = assertThrows(AppException.class, () ->
                characterService.deleteCharacter(id, username)
        );

        assertEquals(AppErrorMessage.NOT_OWNER.getMessage(), exception.getMessage());
        assertEquals(AppErrorMessage.NOT_OWNER.getStatus(), exception.getResponseStatus());
    }

    @Test
    public void getCharacterWithEquipmentSets_shouldReturnCharacterResponseWithCollections(){
        //Before
        long id = 9999;
        CharacterEntity characterEntity = STestUtil.getDefaultCharacterEntity();
        characterEntity.setCharacterEquipmentSets(Collections.singletonList(STestUtil.getDefaultEquipmentEntity()));
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.of(characterEntity));

        //Object
        ServicesUtil servicesUtil = new ServicesUtil(characterRepository,equipmentSetRepository);
        CharacterService characterService = new CharacterService(servicesUtil, characterRepository, null, null);

        //When
        CharacterResponseWithCollections response = characterService.getCharacterWithEquipmentSets(id);

        //Then
        verify(characterRepository, times(1)).findByIdAndActiveIsTrue(any(Long.class));

        assertEquals(characterEntity.getName(), response.getName());
        assertEquals(characterEntity.getId(), response.getId());
        assertEquals(characterEntity.getCharacterEquipmentSets().size(), response.getEquipment().size());
        assertEquals(characterEntity.getCharacterClass().toString(), response.getCharacterClass());
        assertEquals(characterEntity.getCharacterEquipmentSets().get(0).getName(),characterEntity.getCharacterEquipmentSets().get(0).getName());
    }
    @Test
    public void getCharacterWithEquipmentSets_shouldThrowCharacterNotFoundException(){
        //Before
        long id = 9999;
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.empty());

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, null, null);

        //When
        AppException exception = assertThrows(AppException.class, () ->
                characterService.getCharacterWithEquipmentSets(id)
        );

        //Then
        assertEquals(AppErrorMessage.CHARACTER_NOT_FOUND_ID.getMessage(), exception.getMessage());
        assertEquals(AppErrorMessage.CHARACTER_NOT_FOUND_ID.getStatus(), exception.getResponseStatus());
    }

    @Test
    public void editCharacter_shouldReturnEditedCharacter(){
        //Before
        String username = "TestUser";
        long id = 9999;
        CharacterRequest characterRequest = STestUtil.getDefaultCharacterRequest();
        UserEntity userEntity = STestUtil.getDefaultUserEntity();
        CharacterEntity characterEntity = STestUtil.getDefaultCharacterEntity(1);
        characterEntity.setUser(userEntity);
        int numberOfSets = 2;
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.of(characterEntity));
        when(characterRepository.save(any(CharacterEntity.class)))
                .thenReturn(characterEntity);
        when(equipmentSetRepository.getCountOfSetForCharacterId(any(Long.class)))
                .thenReturn(numberOfSets);

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, null, equipmentSetRepository);

        //When
        characterService.editCharacter(characterRequest,username,id);

        //Then
        ArgumentCaptor<CharacterEntity> captor = ArgumentCaptor.forClass(CharacterEntity.class);
        verify(characterRepository).save(captor.capture());

        assertEquals(characterRequest.getCharacterClass(), captor.getValue().getCharacterClass().toString());
        assertEquals(characterRequest.getName(), captor.getValue().getName());
    }
    @Test
    public void editCharacter_shouldReturnNotEditedCharacterBecauseNoDataWasSend(){
        //Before
        String username = "TestUser";
        long id = 9999;
        CharacterRequest characterRequest = new CharacterRequest("",null);
        UserEntity userEntity = STestUtil.getDefaultUserEntity();
        CharacterEntity characterEntity = STestUtil.getDefaultCharacterEntity(1);
        characterEntity.setUser(userEntity);
        int numberOfSets = 2;
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.of(characterEntity));
        when(characterRepository.save(any(CharacterEntity.class)))
                .thenReturn(characterEntity);
        when(equipmentSetRepository.getCountOfSetForCharacterId(any(Long.class)))
                .thenReturn(numberOfSets);

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, null, equipmentSetRepository);

        //When
        characterService.editCharacter(characterRequest,username,id);

        //Then
        ArgumentCaptor<CharacterEntity> captor = ArgumentCaptor.forClass(CharacterEntity.class);
        verify(characterRepository).save(captor.capture());

        assertNotEquals(characterRequest.getCharacterClass(), captor.getValue().getCharacterClass().toString());
        assertNotEquals(characterRequest.getName(), captor.getValue().getName());
    }
    @Test
    public void editCharacter_shouldThrowClassDoNotExist(){
        //Before
        String username = "TestUser";
        long id = 9999;
        CharacterRequest characterRequest = STestUtil.getDefaultCharacterRequest();
        characterRequest.setCharacterClass("NotAClass");
        UserEntity userEntity = STestUtil.getDefaultUserEntity();
        CharacterEntity characterEntity = STestUtil.getDefaultCharacterEntity(1);
        characterEntity.setUser(userEntity);
        when(characterRepository.findByIdAndActiveIsTrue(any(Long.class)))
                .thenReturn(Optional.of(characterEntity));

        //Object
        CharacterService characterService = new CharacterService(null, characterRepository, null, null);

        //When
        AppException exception = assertThrows(AppException.class, () ->
                characterService.editCharacter(characterRequest,username,id)
        );

        //Then
        assertEquals(AppErrorMessage.CLASS_DO_NOT_EXIST.getMessage(), exception.getMessage());
        assertEquals(AppErrorMessage.CLASS_DO_NOT_EXIST.getStatus(), exception.getResponseStatus());
    }

}