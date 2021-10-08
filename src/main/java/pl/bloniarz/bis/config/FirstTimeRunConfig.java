package pl.bloniarz.bis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bloniarz.bis.externalapi.WowheadHttpClient;
import pl.bloniarz.bis.externalapi.model.LootSource;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponse;
import pl.bloniarz.bis.model.dao.item.StatsEquationEntity;
import pl.bloniarz.bis.model.dao.user.UserAuthorityEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.repository.AuthorityRepository;
import pl.bloniarz.bis.repository.StatsEquationRepository;
import pl.bloniarz.bis.repository.UserRepository;
import pl.bloniarz.bis.service.ItemService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class FirstTimeRunConfig implements CommandLineRunner {

    private final WowheadHttpClient wowheadHttpClient;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;
    private final StatsEquationRepository statsEquationRepository;

    @Override
    public void run(String... args) throws Exception {
//        populateAuthoritiesAndCreateAdmin();
//        populateDatabaseWithStatsEquations();
//        populateDatabaseWithWowheadItems();
    }

    private void populateDatabaseWithWowheadItems() {
        List<LootSource> lootSourceList = new LinkedList<>();
        lootSourceList.add(LootSource.builder()
                .sourceName("De Other Side")
                .searchRequirements("220:195;13309:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("Halls of Atonement")
                .searchRequirements("220:195;12831:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("Mists of Trina Scithe")
                .searchRequirements("220:195;13334:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("Plaguefall")
                .searchRequirements("220:195;13228:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("Sanguine Depths")
                .searchRequirements("220:195;12842:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("Spires of Ascension")
                .searchRequirements("220:195;12837:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("The Necrotic Wake")
                .searchRequirements("220:195;12916:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("Theater of Pain")
                .searchRequirements("220:195;12841:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(158)
                .maxIlvl(158)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("Castle Nathria")
                .searchRequirements("212:195;13224:1;0:0")
                .expansion("Shadowlands")
                .minIlvl(200)
                .maxIlvl(207)
                .build());
        lootSourceList.add(LootSource.builder()
                .sourceName("PvP")
                .searchRequirements("129:195;0:1;168011:0")
                .expansion("Shadowlands")
                .minIlvl(200)
                .maxIlvl(200)
                .build());

        lootSourceList.forEach(value -> {
            itemService.addAllItemsToDatabase(wowheadHttpClient.getItemsListForLootSource(value), value.getSourceName());
        });
    }

    private void populateDatabaseWithStatsEquations() throws ParseException {
        List<StatsEquationEntity> stamina = new LinkedList<>();
        stamina.add(StatsEquationEntity.builder()
                .name("STAMINA_HIGH")
                .x3(parseFractionToDouble("6119/625000000"))
                .x2(parseFractionToDouble("42308363/10000000000"))
                .x1(parseFractionToDouble("-7796860951/5000000000"))
                .x0(parseFractionToDouble("1721495577689/10000000000"))
                .build());
        stamina.add(StatsEquationEntity.builder()
                .name("STAMINA_MEDIUM")
                .x3(parseFractionToDouble("53211/10000000000"))
                .x2(parseFractionToDouble("22455371/5000000000"))
                .x1(parseFractionToDouble("-3636469951/2500000000"))
                .x0(parseFractionToDouble("374266079793/2500000000"))
                .build());
        stamina.add(StatsEquationEntity.builder()
                .name("STAMINA_LOW")
                .x3(parseFractionToDouble("1/128000"))
                .x2(parseFractionToDouble("10160509/10000000000"))
                .x1(parseFractionToDouble("-1242219867/2000000000"))
                .x0(parseFractionToDouble("819533854007/10000000000"))
                .build());
        stamina.add(StatsEquationEntity.builder()
                .name("STAMINA_TRINKET")
                .x3(parseFractionToDouble("116449/10000000000"))
                .x2(parseFractionToDouble("23666781/10000000000"))
                .x1(parseFractionToDouble("-11107436031/10000000000"))
                .x0(parseFractionToDouble("1374651416369/10000000000"))
                .build());
        stamina.add(StatsEquationEntity.builder()
                .name("STAMINA_OH")
                .x3(parseFractionToDouble("8499/1250000000"))
                .x2(parseFractionToDouble("4246941/5000000000"))
                .x1(parseFractionToDouble("-1266854959/2500000000"))
                .x0(parseFractionToDouble("335177370653/5000000000"))
                .build());
        statsEquationRepository.saveAll(stamina);
        List<StatsEquationEntity> mainStat = new LinkedList<>();
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_HIGH")
                .x3(parseFractionToDouble("132981/10000000000"))
                .x2(parseFractionToDouble("-13009921/2500000000"))
                .x1(parseFractionToDouble("672183451/625000000"))
                .x0(parseFractionToDouble("-19784326099/400000000"))
                .build());
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_MEDIUM")
                .x3(parseFractionToDouble("1031/100000000"))
                .x2(parseFractionToDouble("-42442669/10000000000"))
                .x1(parseFractionToDouble("9040493917/10000000000"))
                .x0(parseFractionToDouble("-18222779993/400000000"))
                .build());
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_LOW")
                .x3(parseFractionToDouble("3149/2000000000"))
                .x2(parseFractionToDouble("6021923/10000000000"))
                .x1(parseFractionToDouble("-426516539/5000000000"))
                .x0(parseFractionToDouble("20295920721/1250000000"))
                .build());
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_TRINKET")
                .x3(parseFractionToDouble("21197/1250000000"))
                .x2(parseFractionToDouble("-151269/19531250"))
                .x1(parseFractionToDouble("2010494603/1250000000"))
                .x0(parseFractionToDouble("-433279796913/5000000000"))
                .build());
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_INT_TH")
                .x3(parseFractionToDouble("178899/5000000000"))
                .x2(parseFractionToDouble("-44202981/5000000000"))
                .x1(parseFractionToDouble("4718931873/2500000000"))
                .x0(parseFractionToDouble("-66725998199/2500000000"))
                .build());
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_OH")
                .x3(parseFractionToDouble("11747/10000000000"))
                .x2(parseFractionToDouble("160127/200000000"))
                .x1(parseFractionToDouble("-101468511/625000000"))
                .x0(parseFractionToDouble("230266576217/10000000000"))
                .build());
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_INT_OH")
                .x3(parseFractionToDouble("219369/10000000000"))
                .x2(parseFractionToDouble("-763753/156250000"))
                .x1(parseFractionToDouble("1318857759/1250000000"))
                .x0(parseFractionToDouble("-52341166387/10000000000"))
                .build());
        mainStat.add(StatsEquationEntity.builder()
                .name("MAIN_INT_OFF")
                .x3(parseFractionToDouble("39619/2500000000"))
                .x2(parseFractionToDouble("-805353/156250000"))
                .x1(parseFractionToDouble("2140750341/2000000000"))
                .x0(parseFractionToDouble("-369549932351/10000000000"))
                .build());
        statsEquationRepository.saveAll(mainStat);
        List<StatsEquationEntity> secondaryStat = new LinkedList<>();
        secondaryStat.add(StatsEquationEntity.builder()
                .name("SECONDARY_HIGH")
                .x3(parseFractionToDouble("14867/2500000000"))
                .x2(parseFractionToDouble("-38905927/10000000000"))
                .x1(parseFractionToDouble("1951327821/1250000000"))
                .x0(parseFractionToDouble("-202496458687/2500000000"))
                .build());
        secondaryStat.add(StatsEquationEntity.builder()
                .name("SECONDARY_MEDIUM")
                .x3(parseFractionToDouble("-18287/5000000000"))
                .x2(parseFractionToDouble("4497489/2000000000"))
                .x1(parseFractionToDouble("812651083/10000000000"))
                .x0(parseFractionToDouble("38539423541/2500000000"))
                .build());
        secondaryStat.add(StatsEquationEntity.builder()
                .name("SECONDARY_LOW")
                .x3(parseFractionToDouble("-75943/10000000000"))
                .x2(parseFractionToDouble("12127569/2500000000"))
                .x1(parseFractionToDouble("-3091986173/5000000000"))
                .x0(parseFractionToDouble("595491776293/10000000000"))
                .build());
        secondaryStat.add(StatsEquationEntity.builder()
                .name("SECONDARY_AMULET")
                .x3(parseFractionToDouble("96637/10000000000"))
                .x2(parseFractionToDouble("-15535451/2500000000"))
                .x1(parseFractionToDouble("29303987211/10000000000"))
                .x0(parseFractionToDouble("-1204951907451/5000000000"))
                .build());
        secondaryStat.add(StatsEquationEntity.builder()
                .name("SECONDARY_TRINKET")
                .x3(parseFractionToDouble("-36351/5000000000"))
                .x2(parseFractionToDouble("45879689/10000000000"))
                .x1(parseFractionToDouble("-4312913971/10000000000"))
                .x0(parseFractionToDouble("48261307163/1000000000"))
                .build());
        secondaryStat.add(StatsEquationEntity.builder()
                .name("SECONDARY_OH")
                .x3(parseFractionToDouble("7/1000000000"))
                .x2(parseFractionToDouble("-286181/2000000000"))
                .x1(parseFractionToDouble("211492689/500000000"))
                .x0(parseFractionToDouble("-86376843531/5000000000"))
                .build());
        statsEquationRepository.saveAll(secondaryStat);
    }

    private Double parseFractionToDouble(String fraction) throws ParseException{
        Double d = null;
        if (fraction != null) {
            if (fraction.contains("/")) {
                String[] numbers = fraction.split("/");
                if (numbers.length == 2) {
                    BigDecimal d1 = BigDecimal.valueOf(Double.parseDouble(numbers[0]));
                    BigDecimal d2 = BigDecimal.valueOf(Double.parseDouble(numbers[1]));
                    BigDecimal response = d1.divide(d2, MathContext.DECIMAL128);
                    d = response.doubleValue();
                }
            }
            else {
                d = Double.valueOf(fraction);
            }
        }
        if (d == null) {
            throw new ParseException(fraction, 0);
        }
        return d;
    }

    private void populateAuthoritiesAndCreateAdmin(){
        List<UserAuthorityEntity> listOfAuthorities = new LinkedList<>();
        listOfAuthorities.add(authorityRepository.save(UserAuthorityEntity.builder()
                .authority("ROLE_ADMIN")
                .build()));
        listOfAuthorities.add(authorityRepository.save(UserAuthorityEntity.builder()
                .authority("ROLE_USER")
                .build()));

        String password = encoder.encode("adminpassword");
        UserEntity userEntity = UserEntity.builder()
                .email("bloniarzpatryk@gmail.pl")
                .login("administrator")
                .password(password)
                .authorities(listOfAuthorities)
                .build();

        userRepository.save(userEntity);
    }
}
