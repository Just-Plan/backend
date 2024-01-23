package com.jyp.justplan.domain.mbti.domain;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MbtiResult {
    private final Map<MbtiAnswerType, Integer> countMap = new EnumMap<>(MbtiAnswerType.class);

    public MbtiResult(List<MbtiAnswer> answers) {
        for (MbtiAnswerType type : MbtiAnswerType.values()) {
            countMap.put(type, 0);
        }
        for (MbtiAnswer answer : answers) {
            MbtiAnswerType attribute = answer.getAttribute();
            countMap.put(attribute, countMap.getOrDefault(attribute, 0) + 1);
        }
    }

    public String determineMbtiType() {
        StringBuilder mbtiBuilder = new StringBuilder();

        mbtiBuilder.append(getHigherAttribute(countMap, MbtiAnswerType.i, MbtiAnswerType.e));
        mbtiBuilder.append(getHigherAttribute(countMap, MbtiAnswerType.s, MbtiAnswerType.n));
        mbtiBuilder.append(getHigherAttribute(countMap, MbtiAnswerType.t, MbtiAnswerType.f));
        mbtiBuilder.append(getHigherAttribute(countMap, MbtiAnswerType.j, MbtiAnswerType.p));
        return mbtiBuilder.toString();
    }

    private MbtiAnswerType getHigherAttribute(Map<MbtiAnswerType, Integer> countMap, MbtiAnswerType attr1, MbtiAnswerType attr2) {
        return countMap.get(attr1) >= countMap.get(attr2) ? attr1 : attr2;
    }
}
