
	
	
	package com.apps.quantitymeasurement;
	
	



	import java.util.Objects;

	public class Length {

	    public enum LengthUnit {
	        INCHES(1.0),
	        FEET(12.0),
	        YARDS(36.0);

	        private final double toInchesFactor;

	        LengthUnit(double toInchesFactor) {
	            this.toInchesFactor = toInchesFactor;
	        }

	        public double toInches(double value) {
	            return value * toInchesFactor;
	        }

	        public double fromInches(double inches) {
	            return inches / toInchesFactor;
	        }
	    }

	    private final double value;
	    private final LengthUnit unit;

	    public Length(double value, LengthUnit unit) {
	        this.value = value;
	        this.unit = unit;
	    }

	    public double getValue() {
	        return value;
	    }

	    public LengthUnit getUnit() {
	        return unit;
	    }

	    // ✅ Static conversion
	    public static double convert(double value, LengthUnit from, LengthUnit to) {
	        double inches = from.toInches(value);
	        return to.fromInches(inches);
	    }

	    // ✅ Object conversion
	    public Length convertTo(LengthUnit toUnit) {
	        double convertedValue = convert(this.value, this.unit, toUnit);
	        return new Length(convertedValue, toUnit);
	    }

	    // ✅ Addition
	    public Length add(Length other) {
	        double thisInches = this.unit.toInches(this.value);
	        double otherInches = other.unit.toInches(other.value);

	        double sumInches = thisInches + otherInches;

	        double resultValue = this.unit.fromInches(sumInches);
	        return new Length(resultValue, this.unit);
	    }

	    // ✅ Equality (compare via base unit)
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Length)) return false;
	        Length other = (Length) o;

	        double thisInches = this.unit.toInches(this.value);
	        double otherInches = other.unit.toInches(other.value);

	        return Double.compare(thisInches, otherInches) == 0;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(unit.toInches(value));
	    }

	    @Override
	    public String toString() {
	        return value + " " + unit;
	    }
	}