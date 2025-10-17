package com.pawradise.api.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "system_settings")
public class SystemSettings {

    @Id
    private String id;

    // Notification settings
    private boolean newUserEmail = true;
    private boolean newOrderEmail = true;
    private boolean lowStockAlerts = false;

    private String storeAddress;
    private String storeHours;
    private String storePhone;
    private String storeEmail;

    // A constant for the single document's ID
    public static final String SINGLETON_ID = "main_settings";

    public SystemSettings() {
        this.id = SINGLETON_ID;
    }
}