package com.example.flab.soft.shoppingmallfashion.admin;

import lombok.Getter;

@Getter
public enum TestCategory {
    // 뷰티 (Beauty)
    NEW_ARRIVALS_BEAUTY("신상"),
    SKINCARE("스킨케어"),
    SUNCARE("선케어"),
    CLEANSING("클렌징"),
    MAKEUP("메이크업"),
    FRAGRANCE("프레그런스"),
    HAIRCARE("헤어케어"),
    BODYCARE("바디케어"),
    HAIR_DEVICES("헤어기기"),
    SHAVING_RAZORS("쉐이빙/면도기"),
    BEAUTY_DEVICES("뷰티 디바이스"),
    BEAUTY_TOOLS("미용소품"),
    HEALTH_HYGIENE_PRODUCTS("건강/위생용품"),

    // 신발 (Shoes)
    NEW_ARRIVALS_SHOES("신상"),
    SNEAKERS("스니커즈"),
    DRESS_SHOES("구두"),
    SANDALS_SLIPPERS("샌들/슬리퍼"),
    BOOTS_WORKER_SHOES("부츠/워커"),
    SPORTS_SHOES("스포츠화"),
    SHOE_ACCESSORIES("신발용품"),

    // 상의 (Tops)
    NEW_ARRIVALS_TOPS("신상"),
    SWEATSHIRT("맨투맨/스웨트"),
    SHIRTS_BLOUSES("셔츠/블라우스"),
    HOODIES("후드 티셔츠"),
    KNIT_SWEATER("니트/스웨터"),
    PIQUE_POLO_SHIRTS("피케/카라 티셔츠"),
    LONG_SLEEVE_SHIRTS("긴소매 티셔츠"),
    SHORT_SLEEVE_SHIRTS("반소매 티셔츠"),
    SLEEVELESS_SHIRTS("민소매 티셔츠"),
    SPORTS_TOPS("스포츠 상의"),
    OTHER_TOPS("기타 상의"),

    // 아우터 (Outerwear)
    VIEW_ALL_OUTER("전체보기"),
    NEW_ARRIVALS_OUTER("신상"),
    HOODIE_ZIP_UP("후드 집업"),
    BOMBER_MA_1("블루종/MA-1"),
    LEATHER_RIDERS_JACKET("레더/라이더스 재킷"),
    SHEARLING_FUR("무스탕/퍼"),
    TRUCKER_JACKET("트러커 재킷"),
    SUIT_BLAZER_JACKET("슈트/블레이저 재킷"),
    CARDIGAN("카디건"),
    ANORAK_JACKET("아노락 재킷"),
    FLEECE("플리스/뽀글이"),
    TRACK_JACKET("트레이닝 재킷"),
    STADIUM_JACKET("스타디움 재킷"),
    MID_SEASON_COAT("환절기 코트"),
    WINTER_SINGLE_COAT("겨울 싱글 코트"),
    WINTER_DOUBLE_COAT("겨울 더블 코트"),
    WINTER_OTHER_COAT("겨울 기타 코트"),
    LONG_PUFFY_HEAVY_OUTER("롱패딩/헤비 아우터"),
    SHORT_PUFFY_HEAVY_OUTER("숏패딩/헤비 아우터"),
    PUFFY_VEST("패딩 베스트"),
    VEST("베스트"),
    SAFARI_HUNTING_JACKET("사파리/헌팅 재킷"),
    NYLON_COACH_JACKET("나일론/코치 재킷"),
    OTHER_OUTER("기타 아우터"),

    // 바지 (Pants)
    NEW_ARRIVALS_PANTS("신상"),
    DENIM_PANTS("데님 팬츠"),
    COTTON_PANTS("코튼 팬츠"),
    SUIT_SLACKS("슈트 팬츠/슬랙스"),
    JOGGER_PANTS("트레이닝/조거 팬츠"),
    SHORTS("숏 팬츠"),
    LEGGINGS("레깅스"),
    JUMPSUIT_OVERALLS("점프 슈트/오버올"),
    SPORTS_BOTTOMS("스포츠 하의"),
    OTHER_PANTS("기타 바지"),

    // 원피스/스커트 (Dresses/Skirts)
    NEW_ARRIVALS_DRESS_SKIRT("신상"),
    MINI_DRESS("미니원피스"),
    MIDI_DRESS("미디원피스"),
    MAXI_DRESS("맥시원피스"),
    MINI_SKIRT("미니스커트"),
    MIDI_SKIRT("미디스커트"),
    LONG_SKIRT("롱스커트"),

    // 가방 (Bags)
    NEW_ARRIVALS_BAGS("신상"),
    BACKPACK("백팩"),
    MESSENGER_CROSS_BAG("메신저/크로스 백"),
    SHOULDER_BAG("숄더백"),
    TOTE_BAG("토트백"),
    ECO_BAG("에코백"),
    BOSTON_DUFFEL_BAG("보스턴/더플백"),
    WAIST_BAG("웨이스트 백"),
    POUCH_BAG("파우치 백"),
    BRIEF_CASE("브리프 케이스"),
    CARRIER("캐리어"),
    BAG_ACCESSORIES("가방 소품"),
    WALLET_MONEY_CLIP("지갑/머니클립"),
    CLUTCH_BAG("클러치 백"),

    // 패션 소품 (Fashion Accessories)
    VIEW_ALL_ACCESSORIES("전체보기"),
    NEW_ARRIVALS_ACCESSORIES("신상"),
    HAT("모자"),
    SOCKS_LEGWEAR("양말/레그웨어"),
    SUNGLASSES_FRAMES("선글라스/안경테"),
    ACCESSORY("액세서리"),
    WATCHES("시계"),
    JEWELRY("주얼리"),

    // 속옷/홈웨어 (Underwear/Homewear)
    VIEW_ALL_UNDERWEAR("전체보기"),
    NEW_ARRIVALS_UNDERWEAR("신상"),
    WOMENS_UNDERWEAR_TOP("여성 속옷 상의"),
    WOMENS_UNDERWEAR_BOTTOM("여성 속옷 하의"),
    WOMENS_UNDERWEAR_SET("여성 속옷 세트"),
    MENS_UNDERWEAR("남성 속옷"),
    HOMEWEAR("홈웨어"),

    // 스포츠/레저 (Sports/Leisure)
    NEW_ARRIVALS_SPORTS("신상"),
    SPORTS_TOP("상의"),
    SPORTS_BOTTOM("하의"),
    SPORTS_OUTER("아우터"),
    SPORTS_SKIRT("스커트"),
    SPORTS_DRESS("원피스"),
    SPORTS_SET("상하의세트"),
    SWIMWEAR_BEACHWEAR("수영복/비치웨어"),
    SPORTS_EQUIPMENT("기구/용품/장비"),
    SPORTS_BAGS("스포츠가방"),
    SPORTS_ACCESSORIES("스포츠잡화"),
    SPORTS_HAT("스포츠모자");

    private final String categoryName;

    TestCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}

