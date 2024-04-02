package com.project.service.impl;

import com.project.domain.dto.ReaderDTO;
import com.project.domain.dto.ReservationDTO;
import com.project.domain.entity.ReservationEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.domain.mapper.ReaderMapper;
import com.project.repository.BookReservationRepository;
import com.project.repository.ReaderRepository;
import com.project.repository.ReservationRepository;
import com.project.service.ReservationService;
import com.project.util.Constants;
import com.project.util.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;
    private final ReaderRepository readerRepository;
    private final BookReservationRepository bookReservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository repository, ReaderRepository readerRepository,
                                  BookReservationRepository bookReservationRepository) {
        this.repository = repository;
        this.readerRepository = readerRepository;
        this.bookReservationRepository = bookReservationRepository;
    }

    @Override
    public ReservationDTO findById(Integer id) {
        return Constants.RESERVATION_MAPPER.toReservationDTO(repository.findByIdAndDeletedValueFalse(id));
    }

    @Override
    public ReservationDTO findByIdAndDeletedValueFalse(Integer id) {
        return Constants.RESERVATION_MAPPER.toReservationDTO(repository.findByIdAndDeletedValueFalse(id));
    }

    @Override
    public List<ReservationDTO> findByLocalDate(LocalDateTime date) {
        List<ReservationEntity> reservations = repository.findReservationsByLocalDate(date.toLocalDate());
        if(reservations == null){
            throw new ResourceNotFoundException("Reservation with created date: " +
                    date.toLocalDate() + " does not exist.");
        }
        return reservations.stream()
                .map(Constants.RESERVATION_MAPPER::toReservationDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ReservationDTO save(Integer readerId) {
        readerRepository.findByIdAndDeletedValueFalse(readerId);
        ValidationUtils.checkIfReservationAlreadyExists(readerId, repository);

        ReservationDTO request = new ReservationDTO();
        request.setCreatedDate(LocalDateTime.now());
        request.setLastModified(LocalDateTime.now());
        ReaderDTO reader = ReaderMapper.toDTO(readerRepository.findById(readerId));
        request.setReader(reader);
        return Constants.RESERVATION_MAPPER.toReservationDTO(repository
                .save(Constants.RESERVATION_MAPPER.toReservationEntity(request)));
    }

    @Transactional
    @Override
    public ReservationDTO update(Integer id, LocalDateTime modified_date) {
        ReservationDTO reservation = findByIdAndDeletedValueFalse(id);
        reservation.setLastModified(modified_date);
        return Constants.RESERVATION_MAPPER.toReservationDTO(repository
                .update(Constants.RESERVATION_MAPPER.toReservationEntity(reservation)));
    }

    @Override
    public ReservationDTO update(ReservationDTO reservation) {
        return Constants.RESERVATION_MAPPER.toReservationDTO(repository
                .update(Constants.RESERVATION_MAPPER.toReservationEntity(reservation)));
    }

    @Transactional
    @Override
    public ReservationDTO softDelete(Integer id) {
        ReservationEntity reservation = Constants.RESERVATION_MAPPER
                .toReservationEntity(findByIdAndDeletedValueFalse(id));
        ValidationUtils.checkBeforeDeleteToSoftDeleteIfConditionsAreMet(reservation,repository,bookReservationRepository);
        return Constants.RESERVATION_MAPPER.toReservationDTO(repository.softDelete(repository.findById(id)));
    }

    @Transactional
    @Override
    public ReservationDTO delete(Integer id) {
        ReservationEntity reservation = Constants.RESERVATION_MAPPER
                .toReservationEntity(findByIdAndDeletedValueFalse(id));
        if(ValidationUtils.checkBeforeDeleteToSoftDeleteIfConditionsAreMet(reservation,repository,bookReservationRepository)){
            return Constants.RESERVATION_MAPPER.toReservationDTO(repository.softDelete(repository.findById(id)));
        } else {
            return Constants.RESERVATION_MAPPER.toReservationDTO(repository.delete(repository.findById(id)));
        }
    }

    @Override
    public List<ReservationDTO> findReservationsByReaderId(Integer id) {
        readerRepository.findByIdAndDeletedValueFalse(id);
        List<ReservationEntity> reservations = repository.findReservationsByReaderId(id);
        if (reservations == null){
            throw new ResourceNotFoundException("There are no reservations for reader with id: " + id);
        }
        return reservations.stream()
                .map(Constants.RESERVATION_MAPPER::toReservationDTO)
                .toList();
    }

    @Override
    public ReservationDTO findReservationsByReaderIdAndLocalDate(Integer id, LocalDateTime date) {
        readerRepository.findByIdAndDeletedValueFalse(id);
        repository.findReservationsByReaderIdAndLocalDate(id,date.toLocalDate());
        return Constants.RESERVATION_MAPPER
                .toReservationDTO(repository.findReservationsByReaderIdAndLocalDate(id,date.toLocalDate()));
    }

    @Override
    public List<ReservationDTO> getAllReservations(int pageNumber, int pageSize) {
        List<ReservationEntity> list = repository.getAll(pageNumber,pageSize);
        return list.stream()
                .map(Constants.RESERVATION_MAPPER::toReservationDTO)
                .collect(Collectors.toList());
    }

}
