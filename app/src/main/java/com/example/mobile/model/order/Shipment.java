package com.example.mobile.model.order;

import java.io.Serializable;
import java.util.HashMap;

public class Shipment implements Serializable {
    private int id;
    private String typeName;
    private float shipPrice;

    private static final String SHIPMENT_TYPE_STANDARD = "Standard";
    private static final String SHIPMENT_TYPE_NINJA = "Ninja Van";
    private static final String SHIPMENT_TYPE_LIGHTNING = "Lightning Ship";

    private static final float SHIPMENT_PRICE_STANDARD = 10000;
    private static final float SHIPMENT_PRICE_NINJA = 20000;
    private static final float SHIPMENT_PRICE_LIGHTNING = 30000;

    private static HashMap<String, Float> mapShipmentMethod = new HashMap<>();


    public Shipment() {
        mapShipmentMethod.put(SHIPMENT_TYPE_STANDARD, SHIPMENT_PRICE_STANDARD);
        mapShipmentMethod.put(SHIPMENT_TYPE_NINJA, SHIPMENT_PRICE_NINJA);
        mapShipmentMethod.put(SHIPMENT_TYPE_LIGHTNING, SHIPMENT_PRICE_LIGHTNING);
    }

    public Shipment(int id, String typeName, float shipPrice) {
        this.id = id;
        this.typeName = typeName;
        this.shipPrice = shipPrice;
        mapShipmentMethod.put(SHIPMENT_TYPE_STANDARD, SHIPMENT_PRICE_STANDARD);
        mapShipmentMethod.put(SHIPMENT_TYPE_NINJA, SHIPMENT_PRICE_NINJA);
        mapShipmentMethod.put(SHIPMENT_TYPE_LIGHTNING, SHIPMENT_PRICE_LIGHTNING);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
        if(mapShipmentMethod.containsKey(typeName)) {
            this.shipPrice = mapShipmentMethod.get(typeName);
        }
    }

    public float getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(float shipPrice) {
        this.shipPrice = shipPrice;
    }
}
