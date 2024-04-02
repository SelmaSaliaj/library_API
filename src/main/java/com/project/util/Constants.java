package com.project.util;

import com.project.domain.mapper.BookReservationMapper;
import com.project.domain.mapper.ReservationMapper;
import org.mapstruct.factory.Mappers;

import java.util.regex.Pattern;

public class Constants {

    public static final ReservationMapper RESERVATION_MAPPER = Mappers.getMapper(ReservationMapper.class);
    public static final BookReservationMapper BOOK_RESERVATION_MAPPER = Mappers.getMapper(BookReservationMapper.class);

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9]+(?:[._][a-zA-Z0-9]+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,7}$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public static final String DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String SELECT_ALL_EBOOK = "SELECT e FROM EBookEntity e WHERE 1 = 1";
    public static final String SELECT_EBOOK_BY_ID = "SELECT e FROM EBookEntity e WHERE e.id = :id" ;
    public static final String SELECT_EBOOKS_BY_TITLE = "SELECT e FROM EBookEntity e WHERE e.title = :title";
    public static final String SELECT_EBOOKS_BY_AUTHOR = "SELECT e FROM EBookEntity e WHERE e.author = :author";
    public static final String SELECT_EBOOKS_BY_TITLE_AND_AUTHOR = "SELECT e FROM EBookEntity e WHERE e.title = :title AND e.author = :author";

    public static final String SELECT_ALL_LOCATIONS = "SELECT l FROM LocationEntity l WHERE 1 = 1";
    public static final String SELECT_LOCATION_BY_ID = "SELECT l FROM LocationEntity l WHERE l.id = :id" ;
    public static final String SELECT_LOCATION_BY_SHELF_NAME = "SELECT l FROM LocationEntity l WHERE l.nameOfTheShelf = :nameOfTheShelf";

    public static final String SELECT_ALL_BOOKS = "SELECT b FROM PhysicalCopyEntity b WHERE 1 = 1";
    public static final String SELECT_BOOKS_BY_LOCATION_ID = "SELECT b FROM PhysicalCopyEntity b WHERE b.location.id = :id";
    public static final String SELECT_BOOK_BY_ID = "SELECT b FROM PhysicalCopyEntity b WHERE b.id = :id";
    public static final String SELECT_BOOKS_BY_TITLE = "SELECT b FROM PhysicalCopyEntity b WHERE b.title = :title";
    public static final String SELECT_BOOKS_BY_AUTHOR = "SELECT b FROM PhysicalCopyEntity b WHERE b.author = :author";
    public static final String SELECT_BOOKS_BY_TITLE_AND_AUTHOR = "SELECT b FROM PhysicalCopyEntity b WHERE b.title = :title AND b.author = :author";

    public static final String SELECT_ALL_READER = "SELECT r FROM ReaderEntity r WHERE 1 = 1";
    public static final String SELECT_READER_BY_ID = "SELECT r FROM ReaderEntity r WHERE r.id = :id";
    public static final String SELECT_READER_BY_ID_AND_DELETED_FALSE = "SELECT r FROM ReaderEntity r WHERE r.id = :id AND r.deleted = false";
    public static final String SELECT_READER_BY_EMAIL = "SELECT r FROM ReaderEntity r WHERE r.email = :email";

    public static final String SELECT_ALL_RESERVATIONS = "SELECT r FROM ReservationEntity r WHERE 1 = 1";
    public static final String SELECT_RESERVATIONS_BY_READER_ID = "SELECT r FROM ReservationEntity r WHERE r.reader.id = :id AND r.deleted = false";
    public static final String SELECT_RESERVATION_BY_ID = "SELECT r FROM ReservationEntity r WHERE r.id = :id";
    public static final String SELECT_RESERVATION_BY_ID_AND_DELETED_FALSE = "SELECT r FROM ReservationEntity r WHERE r.id = :id AND r.deleted = false";
    public static final String SELECT_RESERVATION_BY_CREATED_DATE = "SELECT r FROM ReservationEntity r WHERE DATE(r.createdDate) = :createdDate";
    public static final String SELECT_RESERVATION_BY_READER_ID_CREATED_DATE = "SELECT r FROM ReservationEntity r WHERE r.reader.id = :id AND DATE(r.createdDate) = :createdDate";

    public static final String SELECT_ALL_BOOK_RESERVATIONS = "SELECT b FROM BookReservationEntity b WHERE 1 = 1";
    public static final String SELECT_BOOK_RESERVATION_BY_ID = "SELECT b FROM BookReservationEntity b WHERE b.id = :id";
    public static final String SELECT_BOOK_RESERVATION_BY_ID_AND_DELETED_FALSE = "SELECT b FROM BookReservationEntity b WHERE b.id = :id AND b.deleted = false";
    public static final String SELECT_BOOK_RESERVATION_BY_RESERVATION_ID = "SELECT b FROM BookReservationEntity b WHERE b.reservation.id = :id AND b.deleted = false";
    public static final String SELECT_BOOK_RESERVATION_BY_RESERVATION_ID_AND_STATUS_NOT_RETURNED = "SELECT b FROM BookReservationEntity b WHERE (b.reservation.id = :id AND b.status != 'RETURNED')";
    public static final String SELECT_BOOK_RESERVATION_BY_BOOK_ID = "SELECT b FROM BookReservationEntity b WHERE b.book.id = :id";
    public static final String SELECT_BOOK_RESERVATION_BY_ID_AND_CREATED_DATE = "SELECT b FROM BookReservationEntity b WHERE b.id = :id AND DATE(b.createdDate) = :createdDate";
    public static final String SELECT_BOOK_RESERVATION_BY_CREATED_DATE = "SELECT b FROM BookReservationEntity b WHERE DATE(b.createdDate) = :createdDate";

    public static final String SELECT_ALL_USER = "SELECT u FROM UserEntity u WHERE 1 = 1";
    public static final String SELECT_USER_BY_ID = "SELECT u FROM UserEntity u WHERE u.id = :id";
    public static final String SELECT_USER_BY_USERNAME = "SELECT u FROM UserEntity u WHERE u.username = :username";

}
