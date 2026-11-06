package com.example.assurancehabitation.service;

import com.example.assurancehabitation.dto.accommodation.AccommodationRequest;
import com.example.assurancehabitation.dto.accommodation.AccommodationResponse;
import com.example.assurancehabitation.entity.assurance.Accommodation;
import com.example.assurancehabitation.entity.assurance.Address;
import com.example.assurancehabitation.entity.assurance.User;
import com.example.assurancehabitation.exception.ResourceNotFoundException;
import com.example.assurancehabitation.repository.AccommodationRepository;
import com.example.assurancehabitation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

    public AccommodationService(AccommodationRepository accommodationRepository,
                                 UserRepository userRepository) {
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
    }

    public AccommodationResponse create(AccommodationRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        Accommodation accommodation = new Accommodation();
        accommodation.setAddress(new Address(request.street(), request.city(), request.country(), request.postalCode()));
        accommodation.setType(request.type());
        accommodation.setSurfaceArea(request.surfaceArea());
        accommodation.setRoomCount(request.roomCount());
        accommodation.setUser(user);

        return toResponse(accommodationRepository.save(accommodation));
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> findAll() {
        return accommodationRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> findByUserId(Long userId) {
        return accommodationRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AccommodationResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public AccommodationResponse update(Long id, AccommodationRequest request) {
        Accommodation accommodation = findEntityById(id);
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        accommodation.setAddress(new Address(request.street(), request.city(), request.country(), request.postalCode()));
        accommodation.setType(request.type());
        accommodation.setSurfaceArea(request.surfaceArea());
        accommodation.setRoomCount(request.roomCount());
        accommodation.setUser(user);

        return toResponse(accommodationRepository.save(accommodation));
    }

    public void delete(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Accommodation not found with id: " + id);
        }
        accommodationRepository.deleteById(id);
    }

    private Accommodation findEntityById(Long id) {
        return accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found with id: " + id));
    }

    private AccommodationResponse toResponse(Accommodation a) {
        return new AccommodationResponse(
                a.getId(),
                a.getAddress().getStreet(),
                a.getAddress().getCity(),
                a.getAddress().getCountry(),
                a.getAddress().getPostalCode(),
                a.getType(),
                a.getSurfaceArea(),
                a.getRoomCount(),
                a.getUser().getId(),
                a.getCreatedAt()
        );
    }
}
