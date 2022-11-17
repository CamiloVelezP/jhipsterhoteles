package com.arquisocios.reserva.service;

import com.arquisocios.reserva.domain.Hotel;
import com.arquisocios.reserva.repository.HotelRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hotel}.
 */
@Service
@Transactional
public class HotelService {

    private final Logger log = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Save a hotel.
     *
     * @param hotel the entity to save.
     * @return the persisted entity.
     */
    public Hotel save(Hotel hotel) {
        log.debug("Request to save Hotel : {}", hotel);
        return hotelRepository.save(hotel);
    }

    /**
     * Update a hotel.
     *
     * @param hotel the entity to save.
     * @return the persisted entity.
     */
    public Hotel update(Hotel hotel) {
        log.debug("Request to update Hotel : {}", hotel);
        return hotelRepository.save(hotel);
    }

    /**
     * Partially update a hotel.
     *
     * @param hotel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Hotel> partialUpdate(Hotel hotel) {
        log.debug("Request to partially update Hotel : {}", hotel);

        return hotelRepository
            .findById(hotel.getId())
            .map(existingHotel -> {
                if (hotel.getNombre() != null) {
                    existingHotel.setNombre(hotel.getNombre());
                }
                if (hotel.getCadena() != null) {
                    existingHotel.setCadena(hotel.getCadena());
                }
                if (hotel.getCiudad() != null) {
                    existingHotel.setCiudad(hotel.getCiudad());
                }
                if (hotel.getDireccion() != null) {
                    existingHotel.setDireccion(hotel.getDireccion());
                }

                return existingHotel;
            })
            .map(hotelRepository::save);
    }

    /**
     * Get all the hotels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Hotel> findAll(Pageable pageable) {
        log.debug("Request to get all Hotels");
        return hotelRepository.findAll(pageable);
    }

    /**
     * Get one hotel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Hotel> findOne(Long id) {
        log.debug("Request to get Hotel : {}", id);
        return hotelRepository.findById(id);
    }

    /**
     * Delete the hotel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hotel : {}", id);
        hotelRepository.deleteById(id);
    }
}
