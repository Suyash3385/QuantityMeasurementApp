package com.apps.quantitymeasurement;
import java.util.Objects;



enum ArithmeticOperation {
    ADD {
        @Override
        double compute(double a, double b) {
            return a + b;
        }
    },
    SUBTRACT {
        @Override
        double compute(double a, double b) {
            return a - b;
        }
    },
    DIVIDE {
        @Override
        double compute(double a, double b) {
            if (b == 0.0) {
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        }
    };

    abstract double compute(double a, double b);
}


public class Quantity<U extends IMeasureable> {

    private final double value; // value of quantity
    private final U unit;       // unit type

    public Quantity(double value, U unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null"); // validation
        if (Double.isNaN(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value); // convert to base
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        if (!this.unit.getClass().equals(targetUnit.getClass())) {
            throw new IllegalArgumentException("Cannot convert between different measurement categories");
        }

        double baseValue = this.unit.convertToBaseUnit(this.value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(convertedValue, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);

        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double converted = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(round(converted), targetUnit);
    }
    
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);

        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double converted = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(round(converted), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }
    
    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity must not be null");
        }
        if (this.unit == null || other.unit == null) {
            throw new IllegalArgumentException("Unit must not be null");
        }
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cannot operate on different measurement categories");
        }
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Values must be finite numbers");
        }
        if (targetUnitRequired && targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
    }
    
    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {

        // NEW: Validate operation support for both units
        this.unit.validateOperationSupport(operation.name());
        other.unit.validateOperationSupport(operation.name());

        double baseThis = this.unit.convertToBaseUnit(this.value);
        double baseOther = other.unit.convertToBaseUnit(other.value);

        return operation.compute(baseThis, baseOther);
    }

    /**
     * Utility method for rounding to 2 decimal places (consistent with UC10/UC11)
     */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // same ref
        if (obj == null || getClass() != obj.getClass()) return false;

        Quantity<?> other = (Quantity<?>) obj;
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 0.01; // compare base
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBaseUnit() * 100.0) / 100.0);
    }
    
    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}