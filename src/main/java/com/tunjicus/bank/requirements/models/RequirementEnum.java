package com.tunjicus.bank.requirements.models;

public enum RequirementEnum {
    EXPERIENCE("EX"), COMPANY_EXPERIENCE("CX"), NET_WORTH("NW"), CURRENT_SALARY("CS");

    final String label;
    RequirementEnum(String label) {
        this.label = label;
    }

    public static RequirementEnum fromString(String label) {
        return switch (label) {
            case "EX" -> EXPERIENCE;
            case "CX" -> COMPANY_EXPERIENCE;
            case "NW" -> NET_WORTH;
            case "CS" -> CURRENT_SALARY;
            default -> throw new IllegalStateException("Unexpected value: " + label);
        };
    }
}
