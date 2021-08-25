package com.tunjicus.bank.requirements.models;

public enum RequirementSpecification {
    R, N, P;

    public static RequirementSpecification fromString(String str) {
        return switch (str) {
            case "R" -> R;
            case "N" -> N;
            case "P" -> P;
            default -> throw new IllegalStateException("Unexpected value: " + str);
        };
    }
}
