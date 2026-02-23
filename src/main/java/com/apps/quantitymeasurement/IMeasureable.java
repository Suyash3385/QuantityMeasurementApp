package com.apps.quantitymeasurement;
@FunctionalInterface
interface SupportsArithmetic {
    boolean isSupported();
}

public interface IMeasureable {

    
    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();

    
    default SupportsArithmetic supportsArithmetic() {
        return () -> true; 
    }

    default void validateOperationSupport(String operation) {
        
    }
}