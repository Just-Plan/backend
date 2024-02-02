package com.jyp.justplan.domain.place.domain;

import java.util.HashMap;
import java.util.Map;

public enum GoogleMapType {
    ACCOUNTING("회계"),
    AIRPORT("공항"),
    AMUSEMENT_PARK("놀이공원"),
    AQUARIUM("수족관"),
    ART_GALLERY("미술관"),
    ATM("ATM"),
    BAKERY("제과점"),
    BANK("은행"),
    BAR("바"),
    BEAUTY_SALON("미용실"),
    BICYCLE_STORE("자전거 가게"),
    BOOK_STORE("서점"),
    BOWLING_ALLEY("볼링장"),
    BUS_STATION("버스 정류장"),
    CAFE("카페"),
    CAMPGROUND("캠핑장"),
    CAR_DEALER("자동차 딜러"),
    CAR_RENTAL("렌터카"),
    CAR_REPAIR("자동차 수리"),
    CAR_WASH("세차장"),
    CASINO("카지노"),
    CEMETERY("묘지"),
    CHURCH("교회"),
    CITY_HALL("시청"),
    CLOTHING_STORE("의류 매장"),
    CONVENIENCE_STORE("편의점"),
    COURTHOUSE("법원"),
    DENTIST("치과"),
    DEPARTMENT_STORE("백화점"),
    DOCTOR("의사"),
    DRUGSTORE("약국"),
    ELECTRICIAN("전기공"),
    ELECTRONICS_STORE("전자제품 매장"),
    EMBASSY("대사관"),
    FIRE_STATION("소방서"),
    FLORIST("꽃집"),
    FUNERAL_HOME("장례식장"),
    FURNITURE_STORE("가구점"),
    GAS_STATION("주유소"),
    GYM("헬스장"),
    HAIR_CARE("미용실"),
    HARDWARE_STORE("철물점"),
    HINDU_TEMPLE("힌두 사원"),
    HOME_GOODS_STORE("가정용품점"),
    HOSPITAL("병원"),
    INSURANCE_AGENCY("보험사"),
    JEWELRY_STORE("보석점"),
    LAUNDRY("세탁소"),
    LAWYER("변호사"),
    LIBRARY("도서관"),
    LIGHT_RAIL_STATION("경전철역"),
    LIQUOR_STORE("주류점"),
    LOCAL_GOVERNMENT_OFFICE("지방 정부 사무소"),
    LOCKSMITH("자물쇠공"),
    LODGING("숙박시설"),
    MEAL_DELIVERY("식사 배달"),
    MEAL_TAKEAWAY("테이크아웃 식사"),
    MOSQUE("모스크"),
    MOVIE_RENTAL("영화 대여점"),
    MOVIE_THEATER("영화관"),
    MOVING_COMPANY("이사 회사"),
    MUSEUM("박물관"),
    NIGHT_CLUB("나이트클럽"),
    PAINTER("페인터"),
    PARK("공원"),
    PARKING("주차장"),
    PET_STORE("애완동물 가게"),
    PHARMACY("약국"),
    PHYSIOTHERAPIST("물리치료사"),
    PLUMBER("배관공"),
    POLICE("경찰서"),
    POST_OFFICE("우체국"),
    PRIMARY_SCHOOL("초등학교"),
    REAL_ESTATE_AGENCY("부동산 중개사"),
    RESTAURANT("식당"),
    ROOFING_CONTRACTOR("지붕공사업자"),
    RV_PARK("RV 공원"),
    SCHOOL("학교"),
    SECONDARY_SCHOOL("중등학교"),
    SHOE_STORE("신발 가게"),
    SHOPPING_MALL("쇼핑몰"),
    SPA("스파"),
    STADIUM("경기장"),
    STORAGE("저장소"),
    STORE("점포"),
    SUBWAY_STATION("지하철역"),
    SUPERMARKET("슈퍼마켓"),
    SYNAGOGUE("시나고그"),
    TAXI_STAND("택시 승강장"),
    TOURIST_ATTRACTION("관광 명소"),
    TRAIN_STATION("기차역"),
    TRANSIT_STATION("대중교통역"),
    TRAVEL_AGENCY("여행사"),
    UNIVERSITY("대학교"),
    VETERINARY_CARE("수의 치료"),
    ZOO("동물원");

    private final String koreanTranslation;

    // 한글 이름을 키로, 해당 열거형 상수를 값으로 하는 맵
    private static final Map<String, GoogleMapType> KOREAN_NAME_MAP = new HashMap<>();

    static {
        // 모든 열거형 상수에 대해 한글 이름과 열거형 상수를 맵에 추가
        for (GoogleMapType type : values()) {
            KOREAN_NAME_MAP.put(type.getKoreanTranslation(), type);
        }
    }

    GoogleMapType(String koreanTranslation) {
        this.koreanTranslation = koreanTranslation;
    }

    public String getKoreanTranslation() {
        return koreanTranslation;
    }

    public static String translateToKorean(String type) {
        try {
            return GoogleMapType.valueOf(type.toUpperCase().replace(" ", "_")).getKoreanTranslation();
        } catch (IllegalArgumentException e) {
            return "알 수 없는 타입";
        }
    }

    // 한글 이름으로 영문 타입명 찾기
    public static String translateFromKorean(String koreanName) {
        GoogleMapType type = KOREAN_NAME_MAP.get(koreanName);
        if (type != null) {
            return type.name().toLowerCase().replace("_", " ");
        } else {
            return "unknown type";
        }
    }
}
