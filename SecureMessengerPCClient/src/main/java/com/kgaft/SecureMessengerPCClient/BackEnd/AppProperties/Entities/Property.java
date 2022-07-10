package com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.Entities;

import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Value;

import java.util.ArrayList;
import java.util.List;

public class Property implements SQLTableEntityI {
    private String propertyName;
    private String propertyValue;

    public Property(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public Property() {
    }

    @Override
    public List<Value> getAllValues() {
        List<Value> properties = new ArrayList<>();
        properties.add(new Value(String.class, propertyName, "propertyName"));
        properties.add(new Value(String.class, propertyValue, "propertyValue"));
        return properties;
    }

    @Override
    public void initWithValues(List<Value> values) {
        values.forEach(value->{
            switch(value.getValueName()){
                case "propertyName":
                    propertyName = value.getValue();
                    break;
                case "propertyValue":
                    propertyValue = value.getValue();
                    break;
            }
        });
    }

    @Override
    public String getTableName() {
        return "AppProperties";
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
