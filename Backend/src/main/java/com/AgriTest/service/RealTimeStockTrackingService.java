package com.AgriTest.service;

import com.AgriTest.dto.RealTimeStockTrackingRequest;
import com.AgriTest.dto.RealTimeStockTrackingResponse;
import com.AgriTest.model.RealTimeStockTracking;
import com.AgriTest.repository.RealTimeStockTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealTimeStockTrackingService {

    @Autowired
    private RealTimeStockTrackingRepository realTimeStockTrackingRepository;

    public RealTimeStockTrackingResponse createRealTimeStockTracking(RealTimeStockTrackingRequest request) {
        RealTimeStockTracking realTimeStockTracking = new RealTimeStockTracking();
        realTimeStockTracking.setProductName(request.getProductName());
        realTimeStockTracking.setBatchNumber(request.getBatchNumber());
        realTimeStockTracking.setTransactionType(request.getTransactionType());
        realTimeStockTracking.setQuantity(request.getQuantity());
        realTimeStockTracking.setUnitOfMeasure(request.getUnitOfMeasure());
        realTimeStockTracking.setDate(request.getDate());
        realTimeStockTracking.setSourceOrDestination(request.getSourceOrDestination());
        realTimeStockTracking.setFilledBy(request.getFilledBy());
        realTimeStockTracking.setComment(request.getComment());

        RealTimeStockTracking savedRealTimeStockTracking = realTimeStockTrackingRepository.save(realTimeStockTracking);
        return mapToResponse(savedRealTimeStockTracking);
    }

    public List<RealTimeStockTrackingResponse> getAllRealTimeStockTracking() {
        return realTimeStockTrackingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RealTimeStockTrackingResponse getRealTimeStockTrackingById(Long id) {
        RealTimeStockTracking realTimeStockTracking = realTimeStockTrackingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RealTimeStockTracking not found with id: " + id));
        return mapToResponse(realTimeStockTracking);
    }

    public RealTimeStockTrackingResponse updateRealTimeStockTracking(Long id, RealTimeStockTrackingRequest request) {
        RealTimeStockTracking existingRealTimeStockTracking = realTimeStockTrackingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RealTimeStockTracking not found with id: " + id));

        existingRealTimeStockTracking.setProductName(request.getProductName());
        existingRealTimeStockTracking.setBatchNumber(request.getBatchNumber());
        existingRealTimeStockTracking.setTransactionType(request.getTransactionType());
        existingRealTimeStockTracking.setQuantity(request.getQuantity());
        existingRealTimeStockTracking.setUnitOfMeasure(request.getUnitOfMeasure());
        existingRealTimeStockTracking.setDate(request.getDate());
        existingRealTimeStockTracking.setSourceOrDestination(request.getSourceOrDestination());
        existingRealTimeStockTracking.setFilledBy(request.getFilledBy());
        existingRealTimeStockTracking.setComment(request.getComment());

        RealTimeStockTracking updatedRealTimeStockTracking = realTimeStockTrackingRepository.save(existingRealTimeStockTracking);
        return mapToResponse(updatedRealTimeStockTracking);
    }

    public void deleteRealTimeStockTracking(Long id) {
        realTimeStockTrackingRepository.deleteById(id);
    }

    private RealTimeStockTrackingResponse mapToResponse(RealTimeStockTracking realTimeStockTracking) {
        RealTimeStockTrackingResponse response = new RealTimeStockTrackingResponse();
        response.setId(realTimeStockTracking.getId());
        response.setProductName(realTimeStockTracking.getProductName());
        response.setBatchNumber(realTimeStockTracking.getBatchNumber());
        response.setTransactionType(realTimeStockTracking.getTransactionType());
        response.setQuantity(realTimeStockTracking.getQuantity());
        response.setUnitOfMeasure(realTimeStockTracking.getUnitOfMeasure());
        response.setDate(realTimeStockTracking.getDate());
        response.setSourceOrDestination(realTimeStockTracking.getSourceOrDestination());
        response.setFilledBy(realTimeStockTracking.getFilledBy());
        response.setComment(realTimeStockTracking.getComment());
        return response;
    }
}
