package com.setu.support.ticket;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tickets.classification")
public class TicketClassificationProperties {
    private boolean autoClassifyOnCreate = true;
    private boolean autoClassifyOnImport = true;
    private boolean allowCreateManualOverride = true;
    private boolean allowImportManualOverride = true;

    public boolean isAutoClassifyOnCreate() {
        return autoClassifyOnCreate;
    }

    public void setAutoClassifyOnCreate(boolean autoClassifyOnCreate) {
        this.autoClassifyOnCreate = autoClassifyOnCreate;
    }

    public boolean isAutoClassifyOnImport() {
        return autoClassifyOnImport;
    }

    public void setAutoClassifyOnImport(boolean autoClassifyOnImport) {
        this.autoClassifyOnImport = autoClassifyOnImport;
    }

    public boolean isAllowCreateManualOverride() {
        return allowCreateManualOverride;
    }

    public void setAllowCreateManualOverride(boolean allowCreateManualOverride) {
        this.allowCreateManualOverride = allowCreateManualOverride;
    }

    public boolean isAllowImportManualOverride() {
        return allowImportManualOverride;
    }

    public void setAllowImportManualOverride(boolean allowImportManualOverride) {
        this.allowImportManualOverride = allowImportManualOverride;
    }
}
