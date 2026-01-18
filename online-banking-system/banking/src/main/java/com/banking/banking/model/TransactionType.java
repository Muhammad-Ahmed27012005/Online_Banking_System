package com.banking.banking.model;

public enum TransactionType {
    DEPOSIT("Deposit", "💰", "success"),
    WITHDRAW("Withdrawal", "💳", "warning"),
    TRANSFER_OUT("Transfer Sent", "📤", "info"),
    TRANSFER_IN("Transfer Received", "📥", "info"),
    INITIAL_DEPOSIT("Initial Deposit", "🏦", "success");

    private final String displayName;
    private final String icon;
    private final String badgeClass;

    TransactionType(String displayName, String icon, String badgeClass) {
        this.displayName = displayName;
        this.icon = icon;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}