package com.example.flab.soft.shoppingmallfashion.admin;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum TestLargeCategory {
    SHOES("신발", Arrays.asList(
            TestCategory.NEW_ARRIVALS_SHOES,
            TestCategory.SNEAKERS,
            TestCategory.DRESS_SHOES,
            TestCategory.SANDALS_SLIPPERS,
            TestCategory.BOOTS_WORKER_SHOES,
            TestCategory.SPORTS_SHOES,
            TestCategory.SHOE_ACCESSORIES
    )),

    TOPS("상의", Arrays.asList(
            TestCategory.NEW_ARRIVALS_TOPS,
            TestCategory.SWEATSHIRT,
            TestCategory.SHIRTS_BLOUSES,
            TestCategory.HOODIES,
            TestCategory.KNIT_SWEATER,
            TestCategory.PIQUE_POLO_SHIRTS,
            TestCategory.LONG_SLEEVE_SHIRTS,
            TestCategory.SHORT_SLEEVE_SHIRTS,
            TestCategory.SLEEVELESS_SHIRTS,
            TestCategory.SPORTS_TOPS,
            TestCategory.OTHER_TOPS
    )),

    OUTER("아우터", Arrays.asList(
            TestCategory.VIEW_ALL_OUTER,
            TestCategory.NEW_ARRIVALS_OUTER,
            TestCategory.HOODIE_ZIP_UP,
            TestCategory.BOMBER_MA_1,
            TestCategory.LEATHER_RIDERS_JACKET,
            TestCategory.SHEARLING_FUR,
            TestCategory.TRUCKER_JACKET,
            TestCategory.SUIT_BLAZER_JACKET,
            TestCategory.CARDIGAN,
            TestCategory.ANORAK_JACKET,
            TestCategory.FLEECE,
            TestCategory.TRACK_JACKET,
            TestCategory.STADIUM_JACKET,
            TestCategory.MID_SEASON_COAT,
            TestCategory.WINTER_SINGLE_COAT,
            TestCategory.WINTER_DOUBLE_COAT,
            TestCategory.WINTER_OTHER_COAT,
            TestCategory.LONG_PUFFY_HEAVY_OUTER,
            TestCategory.SHORT_PUFFY_HEAVY_OUTER,
            TestCategory.PUFFY_VEST,
            TestCategory.VEST,
            TestCategory.SAFARI_HUNTING_JACKET,
            TestCategory.NYLON_COACH_JACKET,
            TestCategory.OTHER_OUTER
    )),

    PANTS("바지", Arrays.asList(
            TestCategory.NEW_ARRIVALS_PANTS,
            TestCategory.DENIM_PANTS,
            TestCategory.COTTON_PANTS,
            TestCategory.SUIT_SLACKS,
            TestCategory.JOGGER_PANTS,
            TestCategory.SHORTS,
            TestCategory.LEGGINGS,
            TestCategory.JUMPSUIT_OVERALLS,
            TestCategory.SPORTS_BOTTOMS,
            TestCategory.OTHER_PANTS
    )),

    DRESSES_SKIRTS("원피스/스커트", Arrays.asList(
            TestCategory.NEW_ARRIVALS_DRESS_SKIRT,
            TestCategory.MINI_DRESS,
            TestCategory.MIDI_DRESS,
            TestCategory.MAXI_DRESS,
            TestCategory.MINI_SKIRT,
            TestCategory.MIDI_SKIRT,
            TestCategory.LONG_SKIRT
    )),

    BAGS("가방", Arrays.asList(
            TestCategory.NEW_ARRIVALS_BAGS,
            TestCategory.BACKPACK,
            TestCategory.MESSENGER_CROSS_BAG,
            TestCategory.SHOULDER_BAG,
            TestCategory.TOTE_BAG,
            TestCategory.ECO_BAG,
            TestCategory.BOSTON_DUFFEL_BAG,
            TestCategory.WAIST_BAG,
            TestCategory.POUCH_BAG,
            TestCategory.BRIEF_CASE,
            TestCategory.CARRIER,
            TestCategory.BAG_ACCESSORIES,
            TestCategory.WALLET_MONEY_CLIP,
            TestCategory.CLUTCH_BAG
    )),

    FASHION_ACCESSORIES("패션소품", Arrays.asList(
            TestCategory.VIEW_ALL_ACCESSORIES,
            TestCategory.NEW_ARRIVALS_ACCESSORIES,
            TestCategory.HAT,
            TestCategory.SOCKS_LEGWEAR,
            TestCategory.SUNGLASSES_FRAMES,
            TestCategory.ACCESSORY,
            TestCategory.WATCHES,
            TestCategory.JEWELRY
    )),

    UNDERWEAR_HOMEWEAR("속옷/홈웨어", Arrays.asList(
            TestCategory.VIEW_ALL_UNDERWEAR,
            TestCategory.NEW_ARRIVALS_UNDERWEAR,
            TestCategory.WOMENS_UNDERWEAR_TOP,
            TestCategory.WOMENS_UNDERWEAR_BOTTOM,
            TestCategory.WOMENS_UNDERWEAR_SET,
            TestCategory.MENS_UNDERWEAR,
            TestCategory.HOMEWEAR
    )),

    SPORTS_LEISURE("스포츠/레저", Arrays.asList(
            TestCategory.NEW_ARRIVALS_SPORTS,
            TestCategory.SPORTS_TOP,
            TestCategory.SPORTS_BOTTOM,
            TestCategory.SPORTS_OUTER,
            TestCategory.SPORTS_SKIRT,
            TestCategory.SPORTS_DRESS,
            TestCategory.SPORTS_SET,
            TestCategory.SWIMWEAR_BEACHWEAR,
            TestCategory.SPORTS_EQUIPMENT,
            TestCategory.SPORTS_BAGS,
            TestCategory.SPORTS_ACCESSORIES,
            TestCategory.SPORTS_HAT
    )),

    BEAUTY("뷰티", Arrays.asList(
            TestCategory.NEW_ARRIVALS_BEAUTY,
            TestCategory.SKINCARE,
            TestCategory.SUNCARE,
            TestCategory.CLEANSING,
            TestCategory.MAKEUP,
            TestCategory.FRAGRANCE,
            TestCategory.HAIRCARE,
            TestCategory.BODYCARE,
            TestCategory.HAIR_DEVICES,
            TestCategory.SHAVING_RAZORS,
            TestCategory.BEAUTY_DEVICES,
            TestCategory.BEAUTY_TOOLS,
            TestCategory.HEALTH_HYGIENE_PRODUCTS
    ));

    private final String largeCategory;
    private final List<TestCategory> subCategories;

    TestLargeCategory(String largeCategory, List<TestCategory> subCategories) {
        this.largeCategory = largeCategory;
        this.subCategories = subCategories;
    }
}


