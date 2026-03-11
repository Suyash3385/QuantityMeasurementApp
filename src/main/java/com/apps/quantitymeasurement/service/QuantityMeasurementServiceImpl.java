package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.core.*;
import com.apps.quantitymeasurement.model.*;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    // Convert DTO → Core Quantity
    private Quantity<IMeasureable> toQuantity(QuantityDTO dto) {

        IMeasureable unit = getCoreUnit(dto.unit);

        return new Quantity<>(dto.value, unit);
    }

    // Convert DTO Unit → Core Unit
    private IMeasureable getCoreUnit(QuantityDTO.IMeasurableUnit dtoUnit) {

        String name = dtoUnit.getUnitName();

        for (LengthUnit u : LengthUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        for (WeightUnit u : WeightUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        for (VolumeUnit u : VolumeUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        for (TemperatureUnit u : TemperatureUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        throw new IllegalArgumentException("Invalid unit: " + name);
    }

    // Convert core Quantity → DTO
    private QuantityDTO toDTO(Quantity<IMeasureable> quantity) {

        return new QuantityDTO(
                quantity.getValue(),
                QuantityDTO.LengthUnit.valueOf(quantity.getUnit().getUnitName())
        );
    }

    @Override
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasureable> quantity1 = toQuantity(q1);
        Quantity<IMeasureable> quantity2 = toQuantity(q2);

        boolean result = quantity1.equals(quantity2);

        return result;
    }

    @Override
    public QuantityDTO convert(QuantityDTO quantityDTO, QuantityDTO.IMeasurableUnit targetUnit) {

        Quantity<IMeasureable> quantity = toQuantity(quantityDTO);

        IMeasureable coreTargetUnit = getCoreUnit(targetUnit);

        Quantity<IMeasureable> result = quantity.convertTo(coreTargetUnit);

        return new QuantityDTO(result.getValue(), targetUnit);
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasureable> quantity1 = toQuantity(q1);
        Quantity<IMeasureable> quantity2 = toQuantity(q2);

        Quantity<IMeasureable> result = quantity1.add(quantity2);

        return new QuantityDTO(result.getValue(), q1.unit);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasureable> quantity1 = toQuantity(q1);
        Quantity<IMeasureable> quantity2 = toQuantity(q2);

        Quantity<IMeasureable> result = quantity1.subtract(quantity2);

        return new QuantityDTO(result.getValue(), q1.unit);
    }

    @Override
    public double divide(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasureable> quantity1 = toQuantity(q1);
        Quantity<IMeasureable> quantity2 = toQuantity(q2);

        return quantity1.divide(quantity2);
    }
}