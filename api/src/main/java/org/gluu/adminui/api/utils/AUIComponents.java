package org.gluu.adminui.api.utils;

public enum AUIComponents {
    AUTHORIZATION_SERVER("Authorization Server"),
    AUTHORIZATION_SERVER_DB("Authorization Server DB");

    private final String value;

    private AUIComponents() {
        this.value = null;
    }

    private AUIComponents(String value) {
        this.value = value;
    }

    public String getParamName() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public static AUIComponents fromString(String param) {
        if (param != null) {
            AUIComponents[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                AUIComponents gt = var1[var3];
                if (param.equals(gt.value)) {
                    return gt;
                }
            }
        }

        return null;
    }
}
